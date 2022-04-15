package nbodies.sim;

import nbodies.sim.data.SimulationData;

public interface Simulator {
	void execute();

	void start();

	void stop();

	boolean isRunning();

	SimulationData getData();
}
