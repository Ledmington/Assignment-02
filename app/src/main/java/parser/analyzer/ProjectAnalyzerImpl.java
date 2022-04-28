package parser.analyzer;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import parser.analyzer.info.ProjectElem;
import parser.analyzer.report.ClassReport;
import parser.analyzer.report.InterfaceReport;
import parser.analyzer.report.PackageReport;
import parser.analyzer.report.ProjectReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.function.Consumer;

public class ProjectAnalyzerImpl implements ProjectAnalyzer {
	public ProjectAnalyzerImpl() {
		Vertx vertx = Vertx.vertx(
				new VertxOptions().setWorkerPoolSize(Runtime.getRuntime().availableProcessors())
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
	}
}
