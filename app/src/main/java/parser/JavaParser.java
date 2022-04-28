package parser;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class JavaParser {
	public static void main (final String[] args) {
		//ProjectAnalyzer pa = new ProjectAnalyzerImpl();
		//ParserFrame gui = new ParserFrame(pa);

		Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(32));

		vertx.deployVerticle(new ParserVerticle(-365));

		vertx.deploymentIDs().forEach(vertx::undeploy);
		Runtime.getRuntime().addShutdownHook(new Thread(vertx::close));
	}
}
