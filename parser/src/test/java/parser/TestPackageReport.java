package parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Paths;

public class TestPackageReport {

    static ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    public static void before(){
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "parser").toString();
    }

    @Test
    public void testPackageReport(){
        var pr = pa.getPackageReport(path);
        while(!pr.isComplete()){}
        assertEquals("a", pr.result().getFullPackageName());
        assertEquals("b", pr.result().getSrcFullFileName());
    }

}
