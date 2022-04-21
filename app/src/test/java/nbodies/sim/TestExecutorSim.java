package nbodies.sim;

import nbodies.Boundary;
import nbodies.sim.data.SimulationData;
import nbodies.sim.data.SimulationDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestExecutorSim {

    private ExecutorSimulator ex;
    private SimulationData data;

    @BeforeEach
    public void setup() {
        data = SimulationData.builder()
                .bounds(new Boundary(-3, 3, -3, 3))
                .bodies(SimulationDataBuilder.randomBodyIn(-1, 1, -1, 1))
                .steps(10)
                .numBodies(10)
                .build();
    }

    @Test
    public void isInitiallyNotRunning() {
        ex = new ExecutorSimulator(data, 1);
        assertFalse(ex.isRunning());
    }

    @Test
    public void isRunningAfterExecute() {
        ex = new ExecutorSimulator(data, 1);
        ex.execute();
        assertTrue(ex.isRunning());
    }

    @Test
    public void isRunningAfterStart() {
        ex = new ExecutorSimulator(data, 1);
        ex.start();
        assertTrue(ex.isRunning());
    }

    @Test
    public void isNotRunningAfterStop() {
        ex = new ExecutorSimulator(data, 1);
        ex.start();
        ex.stop();
        assertFalse(ex.isRunning());
    }
}
