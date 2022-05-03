package parser;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parser.report.classes.ClassReport;
import parser.report.project.ProjectReport;
import parser.report.project.ProjectReportImpl;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestProjectReport {

    private ProjectReport pr;
    static private ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    static public void before(){
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"), "src").toString();
    }

    @Test
    public void testDoesNotAcceptNull() {
        assertThrows(NullPointerException.class, () -> pr = new ProjectReportImpl(null, Map.of()));
        assertThrows(NullPointerException.class, () -> pr = new ProjectReportImpl("abc", null));
    }

    @Test
    public void testMainClassMustBePresent() {
        Map<String, ClassReport> m = Map.of();
        assertThrows(IllegalArgumentException.class, () -> pr = new ProjectReportImpl("main", m));
    }

    @Test
    public void testProjectReport(){
        var pr = pa.getProjectReport(path);
        while(!pr.isComplete()){}  // Intended busy waiting. Add a short sleep inside?
        if(pr.failed()){
            fail(pr.cause());
        }
        assertEquals(10, pr.result().getAllClasses().size());
        //assertEquals(path, pr.result().getSrcFullFileName());
        //assertEquals(5, pr.result().getMethodsInfo().size());
        //assertEquals("getInterfaceReport", pr.result().getMethodsInfo().get(0).getName());
    }
}
