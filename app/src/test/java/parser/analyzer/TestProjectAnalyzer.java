package parser.analyzer;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestProjectAnalyzer {

	@Test
	public void testNullPath() {
		ProjectAnalyzer pa = new ProjectAnalyzerImpl();
		assertThrows(NullPointerException.class,
				() -> pa.analyzeProject(null, p -> {}));
	}

	@Test
	public void testNullCallback() {
		ProjectAnalyzer pa = new ProjectAnalyzerImpl();
		assertThrows(NullPointerException.class,
				() -> pa.analyzeProject("ciaone", null));
	}

	@Test
	public void testUnexistingPath() {
		ProjectAnalyzer pa = new ProjectAnalyzerImpl();
		assertThrows(FileNotFoundException.class,
				() -> pa.analyzeProject("ciaone", p -> {}));
	}
}
