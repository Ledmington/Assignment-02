package parser;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
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
		return null;
	}

	@Override
	public void analyzeProject(String srcProjectFolderName, Consumer<ProjectElem> callback) throws FileNotFoundException {
		Objects.requireNonNull(srcProjectFolderName);
		Objects.requireNonNull(callback);
		if (!new File(srcProjectFolderName).exists()) {
			throw new FileNotFoundException(srcProjectFolderName + " does not exist");
		}

		Future<Buffer> fut = vertx.fileSystem().readFile(srcProjectFolderName);

		System.out.println(fut);
		fut.onComplete((AsyncResult<Buffer> res) -> {
			System.out.println(res);
			System.out.println(res.result().toString());
		});
		System.out.println(fut);
	}
}
