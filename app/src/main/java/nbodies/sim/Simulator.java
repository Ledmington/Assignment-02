package nbodies.sim;

import nbodies.sim.data.SimulationData;

public interface Simulator {

	/**
	 * This method makes the simulator run in background
	 */
	void execute();

	/**
	 * This method restarts the simulator if paused/stopped
	 */
	void start();

	/**
	 * This method stops the simulator
	 */
	void stop();

	boolean isRunning();

	SimulationData getData();
}
