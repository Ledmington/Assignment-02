package parser;

import org.junit.jupiter.api.Test;
import parser.report.classes.ClassReport;
import parser.report.ProjectReport;
import parser.report.ProjectReportImpl;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestProjectReport {

    private ProjectReport pr;

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
}
