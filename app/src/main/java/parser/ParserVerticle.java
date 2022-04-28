package parser;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class ParserVerticle extends AbstractVerticle {

	private final int id;

	public ParserVerticle(final int id) {
		super();
		this.id = id;
	}

	public void start() {
		log("before");

		Future<Integer> res = this.getVertx().executeBlocking(promise -> {
			log("blocking computation started");
			try {
				Thread.sleep(1000);
				promise.complete(id);
			} catch (InterruptedException ignored) {
				promise.fail("exception");
			}
		}, false);
		log("after sending blocking computation");

		res.onComplete(r -> {
			log("result: " + r.result());
		});
	}

	private void log(final String msg) {
		System.out.println("[" + this.id + "] " + msg);
	}
}
