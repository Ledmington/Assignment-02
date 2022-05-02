package parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestClassReport {
    static ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    public static void before(){
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "parser", "ProjectAnalyzerImpl.java").toString();
    }

    @Test
    public void testClassReport(){
        var ir = pa.getClassReport(path);
        while(!ir.isComplete()){}  // Intended busy waiting. Add a short sleep inside?
        assertEquals("parser.ProjectAnalyzerImpl", ir.result().getFullClassName());
        assertEquals(path, ir.result().getSrcFullFileName());
        assertEquals(5, ir.result().getMethodsInfo().size());
        assertEquals("getInterfaceReport", ir.result().getMethodsInfo().get(0).getName());
    }
}
