package parser;

import org.junit.jupiter.api.Test;
import parser.info.FieldInfoImpl;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFieldInfo {
    @Test
    public void cantAcceptNull() {
        assertThrows(NullPointerException.class, () -> new FieldInfoImpl(null, "int"));
        assertThrows(NullPointerException.class, () -> new FieldInfoImpl("name", null));
    }
}
