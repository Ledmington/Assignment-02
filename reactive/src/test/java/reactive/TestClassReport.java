package reactive;

import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import reactive.report.classes.ClassReport;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestClassReport {
    static ProjectAnalyzer pa;
    static String path;

    @BeforeAll
    public static void before() {
        pa = new ProjectAnalyzerImpl();
        path = Paths.get(System.getProperty("user.dir"),
                "src", "main", "java", "reactive", "ProjectAnalyzerImpl.java").toString();
    }

    @Test
    public void testClassReport() {
        final ClassReport cr = pa.getClassReport(path).blockingGet();
        assertEquals("reactive.ProjectAnalyzerImpl", cr.getFullClassName());
        assertEquals(path, cr.getSrcFullFileName());
    }
}
