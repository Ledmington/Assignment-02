package nbodies.sim;

import nbodies.Body;
import nbodies.V2d;
import nbodies.sim.data.SimulationData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ExecutorSimulator extends AbstractSimulator {

	private final ExecutorService executor;

	public ExecutorSimulator(final SimulationData data, final int nThreads) {
		super(data);
		executor = Executors.newFixedThreadPool(nThreads);
	}

	public ExecutorSimulator(final SimulationData data) {
		this(data, Runtime.getRuntime().availableProcessors());
	}

	public void execute() {
		final Map<Body, Future<V2d>> waitingTasks = new HashMap<>();
		final Map<Body, V2d> totalForces = new HashMap<>();

		Runnable endIterationBarrier = () -> {
			// explicit global synchronization at the end of every iteration
			waitingTasks.forEach((key, value) -> {
				try {
					value.get();
				} catch (InterruptedException | ExecutionException ignored) {
				}
			});

			data.nextIteration();
		};

		Runnable updateParameters = () -> {
			totalForces.forEach((b, v) -> {
				Future<V2d> task = executor.submit(() -> {
					V2d acc = new V2d(v).scalarMul(1.0 / b.getMass());
					b.updateVelocity(acc, data.getDelta());
					b.updatePos(data.getDelta());
					b.checkAndSolveBoundaryCollision(getBounds());
					return null;
				});
				waitingTasks.put(b, task);
			});
			totalForces.clear();
			executor.execute(endIterationBarrier);
		};

		Runnable endComputeBarrier = () -> {
			waitingTasks.forEach((key, value) -> {
				try {
					totalForces.put(key, value.get());
				} catch (InterruptedException | ExecutionException ignored) {}
			});
			waitingTasks.clear();
			executor.execute(updateParameters);
		};

		Runnable addForcesToCompute = () -> {
			for (Body b : getBodies()) {
				Future<V2d> task = executor.submit(() -> computeTotalForceOnBody(b));
				waitingTasks.put(b, task);
			}
			executor.execute(endComputeBarrier);
		};

		while (!data.isFinished()) {
			executor.execute(addForcesToCompute);
			executor.shutdown();
			try {
				executor.awaitTermination(1000_000_000, TimeUnit.SECONDS);
			} catch (InterruptedException ignored) {}
		}

		running = true;
	}

	public void start() {
		if (!running) {
			running = true;
		}
	}

	public void stop() {
		if (!running) return;
		executor.shutdown();
		try {
			boolean b = executor.awaitTermination(1000_000_000, TimeUnit.SECONDS);
		} catch (InterruptedException ignored) {}
		running = false;
	}

	public boolean isRunning() {
		return running;
	}
}
