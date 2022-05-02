package nbodies.sim.data;

import java.util.Objects;

public class Iteration {
    private final long steps;
    private long iter = 0;

    public Iteration(final long steps) {
        this.steps = steps;
    }

    public Iteration(final Iteration iteration) {
        this.steps = iteration.steps;
        this.iter = iteration.iter;
    }

    public long getSteps() {
        return steps;
    }

    public synchronized void inc() {
        iter++;
    }

    public synchronized long getIteration() {
        return iter;
    }

    public synchronized boolean isFinished() {
        return iter >= steps;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Iteration iteration = (Iteration) o;
        return steps == iteration.steps && iter == iteration.iter;
    }

    public int hashCode() {
        return Objects.hash(steps, iter);
    }
}
