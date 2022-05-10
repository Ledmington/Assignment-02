package reactive;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactive.report.packages.PackageReport;
import reactive.report.packages.PackageReportBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPackageReportBuilder {

    private PackageReportBuilder prb;

    @BeforeEach
    public void setup() {
        prb = PackageReport.builder();
    }

    @Test
    public void cantBuildTwice() {
        prb.fullFileName("tmp").fullName("package").build();
        assertThrows(IllegalStateException.class, () -> prb.build());
    }
}
