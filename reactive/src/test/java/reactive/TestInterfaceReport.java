package reactive;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInterfaceReport {

    static ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    public static void before() {
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "reactive", "ProjectAnalyzer.java").toString();
    }

    @Test
    public void testInterfaceReport() {
        var ir = pa.getInterfaceReport(path).blockingGet();
        assertEquals("reactive.ProjectAnalyzer", ir.getFullInterfaceName());
        assertEquals(path, ir.getSrcFullFileName());
    }
}
