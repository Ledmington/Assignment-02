package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import parser.info.*;
import parser.report.classes.ClassReport;
import parser.report.classes.ClassReportImpl;
import parser.report.interfaces.InterfaceReport;
import parser.report.interfaces.InterfaceReportImpl;
import parser.report.packages.PackageReport;
import parser.report.packages.PackageReportImpl;
import parser.report.project.ProjectReport;
import parser.report.project.ProjectReportImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

	private final Vertx vertx;
	//private final ParserVerticle pv;

	public ProjectAnalyzerImpl() {
		vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Runtime.getRuntime().availableProcessors()));
		//pv = new ParserVerticle(-365);
		//vertx.deployVerticle(pv);
	}

	private <X, T> List<T> collect(final List<X> fieldsOrMethods, final Function<X, T> mapper) {
		return fieldsOrMethods.stream().map(mapper).toList();
	}

	private List<MethodInfo> collectMethods(final ClassOrInterfaceDeclaration decl) {
		return collect(decl.getMethods(), MethodInfoImpl::new);
	}

	private List<FieldInfo> collectFields(final ClassOrInterfaceDeclaration decl) {
		return collect(decl.getFields(), FieldInfoImpl::new);
	}

	private ClassOrInterfaceDeclaration getFirstInside(final File file) throws FileNotFoundException {
		return new JavaParser()
				.parse(file)
				.getResult()
				.flatMap(result -> result.findFirst(ClassOrInterfaceDeclaration.class))
				.orElseThrow();
	}

	private ClassOrInterfaceDeclaration getFirstInside(final String filePath) throws FileNotFoundException {
		return getFirstInside(new File(filePath));
	}

	@Override
	public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
		return vertx.executeBlocking(handler -> {
			try {
				ClassOrInterfaceDeclaration interDecl = getFirstInside(srcInterfacePath);
				String fullInterfaceName = interDecl.getFullyQualifiedName().orElseThrow();
				List<MethodInfo> methodsInfo = collectMethods(interDecl);
				handler.complete(new InterfaceReportImpl(fullInterfaceName, srcInterfacePath, methodsInfo));
			} catch (FileNotFoundException e) {
				handler.fail("File not found: " + e.getMessage());
			}
		});
	}

	@Override
	public Future<ClassReport> getClassReport(String srcClassPath) {
		return vertx.executeBlocking(handler -> {
			try {
				ClassOrInterfaceDeclaration classDecl = getFirstInside(srcClassPath);
				String className = classDecl.getFullyQualifiedName().orElseThrow();
				List<MethodInfo> methodsInfo = collectMethods(classDecl);
				List<FieldInfo> fieldsInfo = collectFields(classDecl);
				handler.complete(new ClassReportImpl(className, srcClassPath, methodsInfo, fieldsInfo));
			} catch (FileNotFoundException e) {
				handler.fail("File not found: " + e.getMessage());
			}
		});
	}

	@Override
	public Future<PackageReport> getPackageReport(String srcPackagePath) {
		return vertx.executeBlocking(handler -> {
			List<ClassReport> classReports = new CopyOnWriteArrayList<>();
			List<InterfaceReport> interfaceReports = new CopyOnWriteArrayList<>();
			AtomicInteger count = new AtomicInteger(1);
			final List<File> innerFiles = Arrays.stream(Objects.requireNonNull(new File(srcPackagePath).listFiles())).toList();
			String fullPackageName = null;
			for (File file : innerFiles) {
				if (!file.isFile()) continue;
				ClassOrInterfaceDeclaration classInterfaceDecl;
				try {
					CompilationUnit cu = new JavaParser()
							.parse(file)
							.getResult().orElseThrow();
					classInterfaceDecl = cu
							.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow();
					fullPackageName = cu.getPackageDeclaration().orElseThrow().getNameAsString();

					String finalFullPackageName = fullPackageName;

					if(classInterfaceDecl.isInterface()){
						count.incrementAndGet();
						getInterfaceReport(file.getPath()).onSuccess(ir -> {
							interfaceReports.add(ir);
							if(count.decrementAndGet() == 0) {
								handler.complete(new PackageReportImpl(finalFullPackageName, srcPackagePath, classReports, interfaceReports));
							}
						});
					} else if(classInterfaceDecl.isClassOrInterfaceDeclaration()) {
						count.incrementAndGet();
						getClassReport(file.getPath()).onSuccess(cr -> {
							classReports.add(cr);
							if(count.decrementAndGet() == 0) {
								handler.complete(new PackageReportImpl(finalFullPackageName, srcPackagePath, classReports, interfaceReports));
							}
						});
					}
				} catch (Exception ignored) {}
			}
			if(count.decrementAndGet() == 0) {
				handler.complete(new PackageReportImpl(fullPackageName, srcPackagePath, classReports, interfaceReports));
			}
		});
	}

	@Override
	public Future<ProjectReport> getProjectReport(String srcProjectFolderPath) {
		AtomicInteger count = new AtomicInteger(1);
		Map<String, ClassReport> crm = new ConcurrentHashMap<>();
		return vertx.executeBlocking(handler -> {
			AtomicReference<ClassReport> mainClass = new AtomicReference<>();
			Runnable completeIfZero = () -> {
				if(count.decrementAndGet() == 0) {
					handler.complete(new ProjectReportImpl(mainClass.get().getFullClassName(), crm));
				}
			};

			count.incrementAndGet();
			getPackageReport(srcProjectFolderPath).onSuccess(pr -> {
				mainClass.set(
						pr.getClassReports().stream()
								.peek(cr -> crm.put(cr.getFullClassName(), cr))
								.filter(cr -> cr.getMethodsInfo().stream().anyMatch(mi -> mi.getName().equals("main")))
								.findFirst()
								.orElse(new ClassReportImpl("null", "", null, null))
				);
				completeIfZero.run();
			});

			Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderPath).listFiles()))
					.filter(File::isDirectory)
					.peek(f -> count.incrementAndGet())
					.forEach(f -> getProjectReport(f.getPath()).onSuccess(pr -> {
						if(pr.getMainClass() != null) {
							mainClass.set(pr.getMainClass());
						}
						pr.getAllClasses().forEach(cr -> crm.put(cr.getFullClassName(), cr));
						completeIfZero.run();
					}));

			completeIfZero.run();
		});
	}

	@Override
	public void analyzeProject(String srcProjectFolderName, EventBus topic) throws FileNotFoundException {
		Objects.requireNonNull(srcProjectFolderName);
		Objects.requireNonNull(topic);
		if (!new File(srcProjectFolderName).exists()) {
			throw new FileNotFoundException(srcProjectFolderName + " does not exist");
		}

		getPackageReport(srcProjectFolderName).onSuccess(pr -> {
			// Publish package.
			topic.publish("packages", pr);

			// Publish classes.
			for(ClassReport cr : pr.getClassReports()){
				topic.publish("classes", cr);
				// Publish fields.
				for(FieldInfo fi : cr.getFieldsInfo()){
					topic.publish("fields", fi);
				}
				// Publish methods.
				for(MethodInfo mi : cr.getMethodsInfo()){
					topic.publish("methods", mi);
				}
			}

			// Publish interfaces.
			for(InterfaceReport ir : pr.getInterfaceReports()){
				topic.publish("interfaces", ir);
				// Publish interface methods.
				for(MethodInfo mi : ir.getMethodsInfo()){
					topic.publish("methodSignatures", mi);
				}
			}
		});

		// Recursive call on sub folders.
		Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderName).listFiles()))
				.filter(File::isDirectory)
				.forEach(f -> {
					try {
						analyzeProject(f.getPath(), topic);
					} catch (FileNotFoundException ignored) {
						// For sure not missing.
					}
				});
	}
}
