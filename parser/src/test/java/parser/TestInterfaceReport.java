package parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestInterfaceReport {

    static ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    public static void before() {
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "parser", "ProjectAnalyzer.java").toString();
    }

    @Test
    public void testInterfaceReport() {
        var ir = pa.getInterfaceReport(path);
        while (!ir.isComplete()) {
        }  // Intended busy waiting. Add a short sleep inside?
        if (ir.failed()) {
            fail(ir.cause());
        }
        assertEquals("parser.ProjectAnalyzer", ir.result().getFullInterfaceName());
        assertEquals(path, ir.result().getSrcFullFileName());
        assertEquals(5, ir.result().getMethodsInfo().size());
        assertEquals("getInterfaceReport", ir.result().getMethodsInfo().get(0).getName());
    }
}
