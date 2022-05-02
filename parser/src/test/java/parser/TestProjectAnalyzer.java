package parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestProjectAnalyzer {

	private ProjectAnalyzer pa;

	@BeforeEach
	public void setup() {
		pa = new ProjectAnalyzerImpl();
	}

	@Test
	public void testNullPath() {
		assertThrows(NullPointerException.class,
				() -> pa.analyzeProject(null, p -> {}));
	}

	@Test
	public void testNullCallback() {
		assertThrows(NullPointerException.class,
				() -> pa.analyzeProject("ciaone", null));
	}

	@Test
	public void testUnexistingPath() {
		assertThrows(FileNotFoundException.class,
				() -> pa.analyzeProject("ciaone", p -> {}));
	}
}
