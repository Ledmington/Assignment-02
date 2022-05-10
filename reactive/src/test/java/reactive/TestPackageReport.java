package reactive;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactive.report.packages.PackageReport;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPackageReport {

    static ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    public static void before() {
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "reactive", "report", "packages").toString();
    }

    @Test
    public void testPackageReport() {
        final PackageReport pr = pa.getPackageReport(path).blockingGet();
        assertEquals("reactive.report.packages", pr.getFullPackageName());
        assertEquals(path, pr.getSrcFullFileName());
        assertEquals(1, pr.getInterfaceReports().size());
        assertEquals(2, pr.getClassReports().size());
    }

}
