package parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
        var cr = pa.getClassReport(path);
        while(!cr.isComplete()){}  // Intended busy waiting. Add a short sleep inside?
        if(cr.failed()){
            fail(cr.cause());
        }
        assertEquals("parser.ProjectAnalyzerImpl", cr.result().getFullClassName());
        assertEquals(path, cr.result().getSrcFullFileName());
        assertEquals(5, cr.result().getMethodsInfo().size());
        assertEquals("getInterfaceReport", cr.result().getMethodsInfo().get(0).getName());
    }
}
