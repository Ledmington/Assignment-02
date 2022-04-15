package nbodies.body;

import nbodies.Body;
import nbodies.P2d;
import nbodies.V2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestCopyBody {

    private Body b;

    @BeforeEach
    public void setup() {
        b = new Body(new P2d(1,1), new V2d(-1,-1), 10);
    }

    @Test
    public void copyMustHaveEqualContent() {
        Body copy = new Body(b);
        assertEquals(b, copy);
        assertEquals(b.getPos(), copy.getPos());
        assertEquals(b.getMass(), copy.getMass());
        assertEquals(b.getVel(), copy.getVel());
    }

    @Test
    public void copyMustHaveDifferentPointer() {
        Body copy = new Body(b);
        assertFalse(copy == b);
    }
}
