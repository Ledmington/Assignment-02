package parser;

import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TestProjectAnalyzer {

    private ProjectAnalyzer pa;
    private EventBus eb;
    private String path;

    @BeforeEach
    public void setup() {
        pa = new ProjectAnalyzerImpl();
        eb = ((ProjectAnalyzerImpl) pa).getEventBus();
        path = Paths.get(System.getProperty("user.dir"),
                "src").toString();
    }

    @Test
    public void testNullPath() {
        assertThrows(NullPointerException.class,
                () -> pa.analyzeProject(null));
    }

    @Test
    public void testUnexistingPath() {
        assertThrows(FileNotFoundException.class,
                () -> pa.analyzeProject("ciaone"));
    }

    @Test
    public void testAnalyzeProject() {
        AtomicInteger count = new AtomicInteger(0);
        Set<String> elements = new HashSet<>();
        eb.addOutboundInterceptor(h -> {
            count.incrementAndGet();
            elements.add(h.body() != null ? h.body().toString() : "null");
        });
        Future<Integer> messagesCount = null;
        try {
            messagesCount = pa.analyzeProject(path);
        } catch (FileNotFoundException ex) {
            fail(ex.getMessage());
        }
        while (!messagesCount.isComplete()) {
            try {
                Thread.sleep(1); // Intended busy waiting
            } catch (InterruptedException ignored) {
            }
        }
        // Wait for all messages to be received.
        while (count.get() < messagesCount.result()) {
            try {
                Thread.sleep(1); // Intended busy waiting
            } catch (InterruptedException ignored) {
            }
        }
        assertTrue(elements.contains("parser.TestProjectAnalyzer"));
    }
}
