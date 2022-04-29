package parser;

import org.junit.jupiter.api.Test;
import parser.info.MethodInfoImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMethodInfo {
    @Test
    public void cantAcceptNull() {
        assertThrows(NullPointerException.class, () -> new MethodInfoImpl(null, 0, 10));
    }

    @Test
    public void negativeLineNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new MethodInfoImpl("meth", -1, 0));
        assertThrows(IllegalArgumentException.class, () -> new MethodInfoImpl("meth", 0, -1));
    }

    @Test
    public void invalidLineNumbers() {
        assertThrows(IllegalArgumentException.class, () -> new MethodInfoImpl("meth", 13, 12));
    }
}
