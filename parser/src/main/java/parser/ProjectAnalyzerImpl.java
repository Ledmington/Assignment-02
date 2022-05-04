package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.google.common.base.Equivalence;
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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

	private final Vertx vertx;
	//private final ParserVerticle pv;
	private Consumer<ProjectElem> callback;

	public ProjectAnalyzerImpl() {
		vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Runtime.getRuntime().availableProcessors()));
		//pv = new ParserVerticle(-365);
		//vertx.deployVerticle(pv);
	}

	@Override
	public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
		return vertx.executeBlocking(h -> {
			ClassOrInterfaceDeclaration interDecl = null;
			try {
				interDecl = new JavaParser()
						.parse(new File(srcInterfacePath))
						.getResult().flatMap(result -> result
								.findFirst(ClassOrInterfaceDeclaration.class)).get();
			} catch (FileNotFoundException e) {
				h.fail("File no found: " + e.getMessage());
			}
			var fullInterfaceName = interDecl.getFullyQualifiedName().get();
			var methodsInfo = interDecl.getMethods().stream().map(
					method -> (MethodInfo)new MethodInfoImpl(
							method.getNameAsString(),
							method.getName().getBegin().get().line,
							method.getName().getEnd().get().line
					)
			).toList();
			h.complete(new InterfaceReportImpl(fullInterfaceName, srcInterfacePath, methodsInfo));
		});
	}

	@Override
	public Future<ClassReport> getClassReport(String srcClassPath) {
		return vertx.executeBlocking(h -> {
			ClassOrInterfaceDeclaration classDecl = null;
			try {
				classDecl = new JavaParser()
						.parse(new File(srcClassPath))
						.getResult().flatMap(result -> result
								.findFirst(ClassOrInterfaceDeclaration.class)).get();
			} catch (FileNotFoundException e) {
				h.fail("Class not found: " + e.getMessage());
			}
			var className = classDecl.getFullyQualifiedName().get();
			var methodsInfo = classDecl.getMethods().stream().map(
					method -> (MethodInfo) new MethodInfoImpl(
							method.getNameAsString(),
							method.getName().getBegin().get().line,
							method.getName().getEnd().get().line
					)
			).toList();
			var fieldsInfo = classDecl.getFields().stream().map(
					field -> (FieldInfo) new FieldInfoImpl(
							field.getVariables().get(0).getNameAsString(),
							field.getVariables().get(0).getTypeAsString()
					)
			).toList();
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
			for(File file : innerFiles){
				if(file.isFile()){
					ClassOrInterfaceDeclaration classInterfaceDecl = null;
					try{
						CompilationUnit cu = new JavaParser()
								.parse(file)
								.getResult().get();
						classInterfaceDecl = cu
								.findFirst(ClassOrInterfaceDeclaration.class).get();
						fullPackageName = cu.getPackageDeclaration().get().getNameAsString();

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
					}catch (Exception ignored){}
				}
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
			List<ClassReport> mainClass = new ArrayList<>(1);
			mainClass.add(new ClassReportImpl("null", "", null, null));

			count.incrementAndGet();
			getPackageReport(srcProjectFolderPath).onSuccess(pr -> {
				pr.getClassReports().forEach(cr -> {
					crm.put(cr.getFullClassName(), cr);
					for(var method : cr.getMethodsInfo()){
						if(method.getName().equals("main")){
							mainClass.set(0, cr);
						}
					}
				});
				if(count.decrementAndGet() == 0){
					h.complete(new ProjectReportImpl(
							mainClass.get(0).getFullClassName().equals("null") ?
									"null" : mainClass.get(0).getFullClassName(), crm));
				}
			});

			final List<File> innerFiles = Arrays.stream(Objects.requireNonNull(new File(srcProjectFolderPath).listFiles())).toList();
			for(File file : innerFiles){
				if(file.isDirectory()){
					count.incrementAndGet();
					getProjectReport(file.getPath()).onSuccess(pr -> {
						if(pr.getMainClass() != null){
							mainClass.set(0, pr.getMainClass());
						}
						for(ClassReport cr : pr.getAllClasses()){
							crm.put(cr.getFullClassName(), cr);
						}
						if(count.decrementAndGet() == 0){
							h.complete(new ProjectReportImpl(
									mainClass.get(0).getFullClassName().equals("null") ?
											"null" : mainClass.get(0).getFullClassName(), crm));
						}
					});
				}
			}

			if(count.decrementAndGet() == 0){
				h.complete(new ProjectReportImpl(mainClass.get(0).getFullClassName(), crm));
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
