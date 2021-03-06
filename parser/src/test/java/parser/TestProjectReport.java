package parser;

import io.vertx.core.Future;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import parser.report.project.ProjectReport;
import parser.report.project.ProjectReportImpl;

import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestProjectReport {

    static private ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    static public void before() {
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"), "src").toString();
    }

    @Test
    public void testDoesNotAcceptNull() {
        assertThrows(NullPointerException.class, () -> new ProjectReportImpl(null, Map.of()));
        assertThrows(NullPointerException.class, () -> new ProjectReportImpl("abc", null));
    }

    @Test
    public void testProjectReport() {
        Future<ProjectReport> pr = pa.getProjectReport(path);
        while (!pr.isComplete()) {
            try {
                Thread.sleep(1); // Intended busy waiting
            } catch (InterruptedException ignored) {
            }
        }
        if (pr.failed()) {
            fail(pr.cause());
        }
//        assertEquals(19, pr.result().getAllClasses().size());
        assertEquals("parser.AsyncParser", pr.result().getMainClass().getFullClassName());
        assertEquals(1, pr.result()
                .getClassReport(pr.result().getMainClass().getFullClassName())
                .getMethodsInfo()
                .size());
    }
}
