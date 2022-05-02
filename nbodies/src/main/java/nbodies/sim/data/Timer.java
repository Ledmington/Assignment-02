package nbodies.sim.data;

import java.time.Duration;
import java.time.Instant;

public class Timer {
    private Instant beginning;
    private Instant lastIteration;
    private Duration totalTime;

    public Timer() {}

    public Timer(final Timer timer) {
        this.beginning = timer.beginning;
        this.lastIteration = timer.lastIteration;
        this.totalTime = timer.totalTime;
    }

    public synchronized boolean isStarted() {
        return lastIteration != null;
    }

    public synchronized void start() {
        beginning = Instant.now();
        lastIteration = Instant.now();
    }

    public synchronized Duration tick() {
        Instant newIteration = Instant.now();
        Duration timeElapsed = Duration.between(lastIteration, newIteration);
        lastIteration = newIteration;
        return timeElapsed;
    }

    public synchronized void stop() {
        totalTime = Duration.between(beginning, Instant.now());
    }

    public synchronized Duration elapsed() {
        if (beginning == null) {
            return Duration.ZERO;
        }
        return Duration.between(beginning, Instant.now());
    }

    public synchronized Duration getTotalTime() {
        return totalTime;
    }
}
