package parser;

import com.github.javaparser.JavaParser;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.file.OpenOptions;
import parser.info.ProjectElem;
import parser.report.classes.ClassReport;
import parser.report.InterfaceReport;
import parser.report.PackageReport;
import parser.report.project.ProjectReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

	private final Vertx vertx;
	private final ParserVerticle pv;
	private Consumer<ProjectElem> callback;

	public ProjectAnalyzerImpl() {
		vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(Runtime.getRuntime().availableProcessors()));
		pv = new ParserVerticle(-365);
		vertx.deployVerticle(pv);
	}

	@Override
	public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
		return null;
	}

	@Override
	public Future<ClassReport> getClassReport(String srcClassPath) {
		return null;
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

	@Override
	public ParserVerticle getEventLoop() {
		return pv;
	}
}
