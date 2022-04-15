package nbodies;

import nbodies.sim.MultiThreadSimulator;
import nbodies.sim.Simulator;
import nbodies.sim.data.SimulationData;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static nbodies.sim.data.SimulationDataBuilder.randomBodyIn;

/*
	Utility class used just to take times of execution.
 */
public class TestTime {

	private static SimulationData data;

	private static void init(final int nbodies, final int nsteps) {
		data = SimulationData.builder()
				//.threads(1) // uncomment to use serial
				.numBodies(nbodies)
				.bodies(randomBodyIn(-1, 1, -1, 1))
				.bounds(new Boundary(-6, -6, 6, 6))
				.deltaTime(0.01)
				.steps(nsteps)
				.build();

		Simulator sim = new MultiThreadSimulator(data);

		sim.execute();
	}

	public static void main(final String[] args) {
		final List<Integer> bodies = new LinkedList<>(Collections.singleton(5000)); // Must do this ugliness to please JPF
		final List<Integer> steps = new LinkedList<>(Collections.singleton(50000)); // Must do this ugliness to please JPF
		final int attempts = 1;

		for (Integer nb : bodies) {
			for (Integer ns : steps) {
				System.out.print(nb + " bodies, " + ns + " steps\n times: ");
				List<Double> times = new LinkedList<>();
				for (int i = 0; i < attempts; i++) {
					init(nb, ns);

					while (!data.isFinished()) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException ignored) {
						}
					}

					final double seconds = (double) (data.getTotalTime().toMillis()) / 1000;
					System.out.print(seconds + "; ");
					times.add(seconds);
				}
				System.out.println("\nAverage: " + times.stream().mapToDouble(x -> x).average().getAsDouble() + "\n");
			}
		}
	}
}
