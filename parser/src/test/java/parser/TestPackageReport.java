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
                "src", "main", "java", "parser", "report", "packages").toString();
    }

    @Test
    public void testPackageReport(){
        var pr = pa.getPackageReport(path);
        while(!pr.isComplete()){}
        if(pr.failed()){
            fail(pr.cause());
        }
        assertEquals("parser.report.packages", pr.result().getFullPackageName());
        assertEquals(path, pr.result().getSrcFullFileName());
        assertEquals(1, pr.result().getInterfaceReports().size());
        assertEquals(1, pr.result().getClassReports().size());
    }

}
