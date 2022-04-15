package nbodies.view;

import nbodies.NBodies;
import nbodies.sim.data.SimulationData;

/**
 * Simulation view
 *
 * @author aricci
 */
public class SimulationView {

	private final VisualiserFrame frame;

	/**
	 * Creates a view of the specified size (in pixels)
	 */
	public SimulationView(final int w, final int h, final SimulationData data) {
		frame = new VisualiserFrame(w, h);

		Thread displayerThread = new Thread(() -> {
			while (true) {
				display(NBodies.getSimulator().getData());
				try {
					Thread.sleep(50);
				} catch (InterruptedException ignored) {
				}
			}
		});
		displayerThread.start();
	}

	public void display(final SimulationData data) {
		frame.display(data);
	}
}
