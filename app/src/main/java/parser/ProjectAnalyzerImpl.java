package parser;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.file.OpenOptions;
import parser.info.ProjectElem;
import parser.report.ClassReport;
import parser.report.InterfaceReport;
import parser.report.PackageReport;
import parser.report.ProjectReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.function.Consumer;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {

	private final Vertx vertx;

	public ProjectAnalyzerImpl() {
		vertx = Vertx.vertx(
				//new VertxOptions().setWorkerPoolSize(Runtime.getRuntime().availableProcessors())
		);
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
		return null;//return vertx.fileSystem().open(srcProjectFolderPath, new OpenOptions());
	}

	@Override
	public void analyzeProject(String srcProjectFolderName, Consumer<ProjectElem> callback) throws FileNotFoundException {
		Objects.requireNonNull(srcProjectFolderName);
		Objects.requireNonNull(callback);
		if (!new File(srcProjectFolderName).exists()) {
			throw new FileNotFoundException(srcProjectFolderName + " does not exist");
		}

		Future<ProjectReport> fut = getProjectReport(srcProjectFolderName);

		fut.onComplete(System.out::println);
	}
}
