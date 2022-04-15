package nbodies.sim.data;

import nbodies.Body;
import nbodies.Boundary;
import nbodies.utils.barrier.Barrier;
import nbodies.utils.barrier.ReusableBarrier;
import nbodies.utils.stats.Statistics;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class SimulationData {
	private final int nThreads;
	private final ArrayList<Body> bodies;
	private final Boundary bounds;
	private final double dt;
	private final Iteration iteration;
	private final Barrier pause;
	private final Statistics FPSstats;
	private double vt = 0;
	private final Timer timer;

	public SimulationData(final ArrayList<Body> bodies, final Boundary bounds, final double dt, final long nsteps, final int nThreads) {
		this.nThreads = nThreads;
		this.bodies = bodies;
		this.bounds = bounds;
		this.dt = dt;
		this.iteration = new Iteration(nsteps);
		this.FPSstats = new Statistics();
		pause = new ReusableBarrier(nThreads + 1);
		this.timer = new Timer();
	}

	public SimulationData(final SimulationData data) {
		this.nThreads = data.nThreads;
		this.bodies = data.bodies.stream().map(Body::new).collect(Collectors.toCollection(ArrayList::new));
		this.bounds = new Boundary(data.bounds);
		this.dt = data.dt;
		this.iteration = new Iteration(data.iteration);
		this.pause = data.pause; // No need to deep copy the barrier
		this.vt = data.vt;
		this.FPSstats = data.FPSstats;
		this.timer = new Timer(data.timer);
	}

	public SimulationData(final ArrayList<Body> bodies, final Boundary bounds) {
		this(bodies, bounds, 0.001, 50000, Runtime.getRuntime().availableProcessors());
	}

	public static SimulationDataBuilder builder() {
		return new SimulationDataBuilder();
	}

	public void nextIteration() {
		if (isFinished()) return;

		vt += dt;
		iteration.inc();

		if (!timer.isStarted()) {
			timer.start();
		} else {
			FPSstats.add((double) (timer.tick().toMillis()));
		}

		if (isFinished()) {
			timer.stop();
		}
	}

	public ArrayList<Body> getBodies() {
		return bodies;
	}

	public Boundary getBounds() {
		return bounds;
	}

	public double getTime() {
		return vt;
	}

	public double getDelta() {
		return dt;
	}

	public long getIteration() {
		return iteration.getIteration();
	}

	public int getNThreads() {
		return nThreads;
	}

	public Barrier getPause() {
		return pause;
	}

	public boolean isFinished() {
		return iteration.isFinished();
	}

	public Statistics getFPSStats() {
		return FPSstats;
	}

	public String getETA() {
		Duration elapsed = timer.elapsed();
		double msForEachIteration = (double) elapsed.toMillis() / (double) getIteration();
		long remainingMillis = (long) (msForEachIteration * (iteration.getSteps() - iteration.getIteration()));
		long minutes = remainingMillis / 60_000;
		remainingMillis %= 60_000;
		long seconds = remainingMillis / 1000;
		remainingMillis %= 1000;
		return String.format("%2d:%02d:%03d", minutes, seconds, remainingMillis);
	}

	public Duration getTotalTime() {
		return timer.getTotalTime();
	}

	public String toString() {
		return "Simulating " + bodies.size() + " bodies\n" +
				"Steps: " + iteration.getSteps() + "\n" +
				"Boundaries:\n" +
				"\tx: [" + bounds.getXMin() + ", " + bounds.getXMax() + "]\n" +
				"\ty: [" + bounds.getYMin() + ", " + bounds.getYMax() + "]\n" +
				"delta time: " + dt;
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SimulationData that = (SimulationData) o;
		return nThreads == that.nThreads &&
				Double.compare(that.dt, dt) == 0 &&
				Double.compare(that.vt, vt) == 0 &&
				iteration.equals(that.iteration) &&
				bodies.equals(that.bodies) &&
				bounds.equals(that.bounds) &&
				pause.equals(that.pause) &&
				FPSstats.equals(that.FPSstats);
	}

	public int hashCode() {
		return Objects.hash(nThreads, bodies, bounds, dt, iteration, pause, FPSstats, vt, timer);
	}
}
