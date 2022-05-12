package reactive;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactive.report.project.ProjectReport;
import reactive.report.project.ProjectReportImpl;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        ProjectReport pr = pa.getProjectReport(path).blockingGet();
        assertEquals("reactive.ReactiveParser", pr.getMainClass().getFullClassName());
        assertEquals(1, pr
                .getClassReport(pr.getMainClass().getFullClassName())
                .getMethodsInfo()
                .size());
    }
}
