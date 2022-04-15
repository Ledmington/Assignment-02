package nbodies;

import nbodies.sim.ExecutorSimulator;
import nbodies.sim.Simulator;
import nbodies.sim.data.SimulationData;
import nbodies.view.SimulationView;

import java.awt.*;

import static java.lang.Integer.min;
import static nbodies.sim.data.SimulationDataBuilder.randomBodyIn;

public class NBodies {

	private static final boolean USE_GUI = false;
	private static Simulator sim;

	public static void main(String[] args) {
		if (USE_GUI) {
			System.out.println("Press Start to start the simulation.\n");
		}

		System.out.println(Runtime.getRuntime().availableProcessors() + " cores available");

		final SimulationData data;

		//data = SimulationDataFactory.testBodySet4_many_bodies();
		data = SimulationData.builder()
				//.threads(1) // uncomment to use serial
				.numBodies(1000)
				.bodies(randomBodyIn(-1, 1, -1, 1))
				.bounds(new Boundary(-6, -6, 6, 6))
				.deltaTime(0.01)
				.steps(100_000)
				.build();
		//data = SimulationDataFactory.circle(100);

		System.out.println(data);
		System.out.println("Repulsive constant: " + Body.REPULSIVE_CONST);
		System.out.println("Friction constant: " + Body.FRICTION_CONST);

		sim = new ExecutorSimulator(data);

		if (USE_GUI) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = (int) screenSize.getWidth();
			int height = (int) screenSize.getHeight();
			int size = min(width, height) - 100;
			new SimulationView(size, size, data);
		} else {
			sim.execute();
		}

		// Printing total time
		while (!data.isFinished()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException ignored) {
			}
		}

		double seconds = 0;
		if (data.getTotalTime() != null) {
			seconds = (double) (data.getTotalTime().toMillis()) / 1000;
		}
		System.out.println("\nTotal time of execution: " + seconds + " seconds");
	}

	public static Simulator getSimulator() {
		return sim;
	}
}
