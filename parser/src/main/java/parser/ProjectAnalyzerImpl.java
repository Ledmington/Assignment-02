package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
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
	private Consumer<ProjectElem> callback;

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

	private ClassOrInterfaceDeclaration getFirstInside(final String filePath) throws FileNotFoundException {
		return new JavaParser()
				.parse(new File(filePath))
				.getResult()
				.flatMap(result -> result.findFirst(ClassOrInterfaceDeclaration.class))
				.orElseThrow();
	}

	@Override
	public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
		return vertx.executeBlocking(h -> {
			ClassOrInterfaceDeclaration interDecl = null;
			try {
				interDecl = getFirstInside(srcInterfacePath);
			} catch (FileNotFoundException e) {
				h.fail("File no found: " + e.getMessage());
			}
			String fullInterfaceName = interDecl.getFullyQualifiedName().orElseThrow();
			List<MethodInfo> methodsInfo = collectMethods(interDecl);
			h.complete(new InterfaceReportImpl(fullInterfaceName, srcInterfacePath, methodsInfo));
		});
	}

	@Override
	public Future<ClassReport> getClassReport(String srcClassPath) {
		return vertx.executeBlocking(h -> {
			ClassOrInterfaceDeclaration classDecl = null;
			try {
				classDecl = getFirstInside(srcClassPath);
			} catch (FileNotFoundException e) {
				h.fail("Class not found: " + e.getMessage());
			}
			String className = classDecl.getFullyQualifiedName().orElseThrow();
			List<MethodInfo> methodsInfo = collectMethods(classDecl);
			List<FieldInfo> fieldsInfo = collectFields(classDecl);
			h.complete(new ClassReportImpl(className, srcClassPath, methodsInfo, fieldsInfo));
		});
	}

	@Override
	public Future<PackageReport> getPackageReport(String srcPackagePath) {
		return vertx.executeBlocking(h -> {
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
						getInterfaceReport(file.getPath()).onSuccess(ir ->{
							interfaceReports.add(ir);
							if(count.decrementAndGet() == 0){
								h.complete(new PackageReportImpl(finalFullPackageName, srcPackagePath, classReports, interfaceReports));
							}
						});
					} else if(classInterfaceDecl.isClassOrInterfaceDeclaration()){
						count.incrementAndGet();
						getClassReport(file.getPath()).onSuccess(cr ->{
							classReports.add(cr);
							if(count.decrementAndGet() == 0){
								h.complete(new PackageReportImpl(finalFullPackageName, srcPackagePath, classReports, interfaceReports));
							}
						});
					}
				} catch (Exception ignored) {}
			}
			if(count.decrementAndGet() == 0){
				h.complete(new PackageReportImpl(fullPackageName, srcPackagePath, classReports, interfaceReports));
			}
		});
	}

	@Override
	public Future<ProjectReport> getProjectReport(String srcProjectFolderPath) {
		AtomicInteger count = new AtomicInteger(1);
		Map<String, ClassReport> crm = new ConcurrentHashMap<>();
		return vertx.executeBlocking(h -> {
			AtomicReference<String> mainClass = new AtomicReference<>();

			count.incrementAndGet();
			getPackageReport(srcProjectFolderPath).onSuccess(pr -> {
				mainClass.set(
						pr.getClassReports().stream()
								.peek(cr -> crm.put(cr.getFullClassName(), cr))
								.map(ClassReport::getMethodsInfo)
								.flatMap(Collection::stream)
								.map(MethodInfo::getName)
								.filter(mi -> mi.equals("main"))
								.findFirst()
								.orElse("null")
				);
				if(count.decrementAndGet() == 0){
					h.complete(new ProjectReportImpl(mainClass.get(), crm));
				}
			});

			final List<File> innerFiles = Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderPath).listFiles())).toList();
			for(File file : innerFiles){
				if(file.isDirectory()){
					count.incrementAndGet();
					getProjectReport(file.getPath()).onSuccess(pr -> {
						if(pr.getMainClass() != null){
							mainClass.set(pr.getMainClass().getFullClassName());
						}
						for(ClassReport cr : pr.getAllClasses()){
							crm.put(cr.getFullClassName(), cr);
						}
						if(count.decrementAndGet() == 0){
							h.complete(new ProjectReportImpl(mainClass.get(), crm));
						}
					});
				}
			}

			if(count.decrementAndGet() == 0){
				h.complete(new ProjectReportImpl(mainClass.get(), crm));
			}
		});
	}

	@Override
	public void analyzeProject(String srcProjectFolderName, Consumer<ProjectElem> callback) throws FileNotFoundException {
		Objects.requireNonNull(srcProjectFolderName);
		Objects.requireNonNull(callback);
		if (!new File(srcProjectFolderName).exists()) {
			throw new FileNotFoundException(srcProjectFolderName + " does not exist");
		}

		AtomicInteger count = new AtomicInteger(1);
		vertx.executeBlocking(h -> {
			getPackageReport(srcProjectFolderName).onSuccess(pr -> {
				callback.accept(pr);
			});
		});
	}
}
