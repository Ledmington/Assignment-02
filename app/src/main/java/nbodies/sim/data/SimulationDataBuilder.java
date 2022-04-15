package nbodies.sim.data;

import nbodies.*;

import java.util.ArrayList;
import java.util.function.Supplier;

public class SimulationDataBuilder {

	private Supplier<Body> bodySupplier;
	private long nBodies = 1000;
	private Boundary bounds;
	private double dt = 0.001;
	private long steps = 50000;
	private int nth = Runtime.getRuntime().availableProcessors();

	public static Supplier<Body> randomBodyIn(final double xmin, final double xmax, final double ymin, final double ymax) {
		// added this ugliness to please JPF
		return new Supplier<Body>() {
			public Body get() {
				final P2d pos = new P2d(randomDouble(xmin, xmax), randomDouble(ymin, ymax));
				final V2d vel = new V2d(0, 0);
				final double mass = 10;
				return new Body(pos, vel, mass);
			}
		};
	}

	private static double randomDouble(final double a, final double b) {
		return Math.random() * (b - a) + a;
	}

	public SimulationDataBuilder bodies(final Supplier<Body> bodyCreator) {
		bodySupplier = bodyCreator;
		return this;
	}

	public SimulationDataBuilder numBodies(final long n) {
		nBodies = n;
		return this;
	}

	public SimulationDataBuilder bounds(final Boundary boundaries) {
		this.bounds = boundaries;
		return this;
	}

	public SimulationDataBuilder deltaTime(final double dt) {
		this.dt = dt;
		return this;
	}

	public SimulationDataBuilder steps(final long steps) {
		this.steps = steps;
		return this;
	}

	public SimulationDataBuilder threads(final int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Number of threads can't be negative or zero.");
		}
		this.nth = n;
		return this;
	}

	public SimulationData build() {
		if (bodySupplier == null) {
			throw new IllegalStateException("Cannot build SimulationData without bodies");
		}
		if (bounds == null) {
			throw new IllegalStateException("Cannot build SimulationData without boundaries");
		}

		ArrayList<Body> bodies = new ArrayList<>();
		for (int i = 0; i < nBodies; i++) {
			bodies.add(bodySupplier.get());
		}

		return new SimulationData(bodies, bounds, dt, steps, nth);
	}
}
