package nbodies.sim;

import nbodies.sim.data.SimulationData;

public interface Simulator {

    /**
     * This method makes the simulator run in background
     */
    void execute();

    SimulationData getData();
}
