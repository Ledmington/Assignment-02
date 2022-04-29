package parser;

import com.github.javaparser.JavaParser;
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
import parser.report.project.ProjectReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

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
			var interDecl = new JavaParser()
					.parse(srcInterfacePath)
					.getResult().get()
					.findFirst(ClassOrInterfaceDeclaration.class).get();
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
			var classDecl = new JavaParser()
					.parse(srcClassPath)
					.getResult().get()
					.findFirst(ClassOrInterfaceDeclaration.class).get();
			var className = classDecl.getFullyQualifiedName().get();
			var methodsInfo = classDecl.getMethods().stream().map(
					method -> (MethodInfo)new MethodInfoImpl(
							method.getNameAsString(),
							method.getName().getBegin().get().line,
							method.getName().getEnd().get().line
					)
			).toList();
			var fieldsInfo = classDecl.getFields().stream().map(
					field -> (FieldInfo)new FieldInfoImpl(
							field.getVariables().get(0).getNameAsString(),
							field.getVariables().get(0).getTypeAsString()
					)
			).toList();
			h.complete(new ClassReportImpl(className, srcClassPath, methodsInfo, fieldsInfo));
		});
	}

	@Override
	public Future<PackageReport> getPackageReport(String srcPackagePath) {
		return null;
	}

	@Override
	public Future<ProjectReport> getProjectReport(String srcProjectFolderPath) {
		File project = new File(srcProjectFolderPath);
		if (project.isFile()) {
			//return vertx.fileSystem().open(srcProjectFolderPath, new OpenOptions().setRead(true));
			return vertx.executeBlocking(h -> {
				System.out.println("Parsing \"" + project + "\"");
				try {
					new JavaParser().parse(project);
				} catch (FileNotFoundException e) {
					throw new RuntimeException(e);
				}
			});
		}
		else if (project.isDirectory()) {
			return vertx.executeBlocking(h -> {
				System.out.println("Found directory \"" + srcProjectFolderPath + "\"");
				final List<File> innerFiles = Arrays.stream(Objects.requireNonNull(project.listFiles())).toList();
				for(File file : innerFiles) {
					vertx.executeBlocking(handler -> this.getProjectReport(file.getAbsolutePath()));
				}
			});
		}
		else {
			// maybe it is a symbolic link
			throw new Error("Unknown file type");
		}
	}

	@Override
	public void analyzeProject(String srcProjectFolderName, Consumer<ProjectElem> callback) throws FileNotFoundException {
		Objects.requireNonNull(srcProjectFolderName);
		Objects.requireNonNull(callback);
		if (!new File(srcProjectFolderName).exists()) {
			throw new FileNotFoundException(srcProjectFolderName + " does not exist");
		}

		this.callback = callback;
		Future<ProjectReport> fut = getProjectReport(srcProjectFolderName);

		fut.onComplete(System.out::println);
	}
}
