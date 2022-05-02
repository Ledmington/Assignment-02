package nbodies.boundary;

import nbodies.Boundary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestBoundaries {

    private Boundary b;

    @BeforeEach
    public void setup() {
        b = new Boundary(-1, -1, 1, 1);
    }

    @Test
    public void copyMustHaveEqualContent() {
        Boundary copy = new Boundary(b);
        assertEquals(b, copy);
        assertEquals(b.getXMin(), copy.getXMin());
        assertEquals(b.getXMax(), copy.getXMax());
        assertEquals(b.getYMin(), copy.getYMin());
        assertEquals(b.getYMax(), copy.getYMax());
    }

    @Test
    public void copyMustHaveDifferentPointer() {
        Boundary copy = new Boundary(b);
        assertFalse(copy == b);
    }
}
