package parser;

import org.junit.jupiter.api.Test;
import parser.report.ClassReport;
import parser.report.ClassReportBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClassReportBuilder {

    private ClassReportBuilder crb;

    @Test
    public void hasEmptyLists() {
        ClassReport cr = ClassReport.builder()
                .className("main")
                .fileName("src")
                .build();
        assertTrue(cr.getMethodsInfo().isEmpty());
        assertTrue(cr.getFieldsInfo().isEmpty());
    }

    @Test
    public void testCantBuildTwice() {
        crb = ClassReport.builder()
                .className("main")
                .fileName("src");
        crb.build();
        assertThrows(IllegalStateException.class, () -> crb.build());
    }
}
