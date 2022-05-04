package parser;

import com.google.common.base.Supplier;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.impl.EventBusImpl;
import io.vertx.core.impl.VertxInternal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class TestProjectAnalyzer {

	private ProjectAnalyzer pa;

	@BeforeEach
	public void setup() {
		pa = new ProjectAnalyzerImpl();
	}

	@Test
	public void testNullPath() {
		assertThrows(NullPointerException.class,
				() -> pa.analyzeProject(null, new EventBusImpl((VertxInternal) Vertx.vertx())));
	}

	@Test
	public void testNullCallback() {
		assertThrows(NullPointerException.class,
				() -> pa.analyzeProject("ciaone", null));
	}

	@Test
	public void testUnexistingPath() {
		assertThrows(FileNotFoundException.class,
				() -> pa.analyzeProject("ciaone", new EventBusImpl((VertxInternal) Vertx.vertx())));
	}

	@Test
	public void testAnalyzeProject() {
		
	}
}
