package nbodies.sim;

import nbodies.Boundary;
import nbodies.sim.data.SimulationData;
import nbodies.sim.data.SimulationDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestData {

	private SimulationData data;

	@BeforeEach
	public void setup() {
		data = SimulationData.builder()
				.bodies(SimulationDataBuilder.randomBodyIn(-1, 1, -1, 1))
				.bounds(new Boundary(-3, -3, 3, 3))
				.build();
	}

	@Test
	public void copyMustHaveEqualContent() {
		SimulationData copy = new SimulationData(data);
		assertEquals(data, copy);
		assertEquals(data.getDelta(), copy.getDelta());
		assertEquals(data.getNThreads(), copy.getNThreads());
		assertEquals(data.getBodies(), copy.getBodies());
		assertEquals(data.getBounds(), copy.getBounds());
		assertEquals(data.getIteration(), copy.getIteration());
		assertEquals(data.getTime(), copy.getTime());
	}

	@Test
	public void copyMustHaveDifferentPointer() {
		SimulationData copy = new SimulationData(data);
		assertFalse(copy == data);
	}
}
