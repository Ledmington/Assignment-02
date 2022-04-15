package nbodies;

import java.util.Objects;

/**
 * Boundary of the field where bodies move.
 */
public class Boundary {

	private final double xMin;
	private final double yMin;
	private final double xMax;
	private final double yMax;

	public Boundary(double x0, double y0, double x1, double y1) {
		this.xMin = Math.min(x0, x1);
		this.yMin = Math.min(y0, y1);
		this.xMax = Math.max(x0, x1);
		this.yMax = Math.max(y0, y1);
	}

	public Boundary(final Boundary bounds) {
		this.xMin = bounds.xMin;
		this.xMax = bounds.xMax;
		this.yMin = bounds.yMin;
		this.yMax = bounds.yMax;
	}

	public double getXMin() {
		return xMin;
	}

	public double getXMax() {
		return xMax;
	}

	public double getYMin() {
		return yMin;
	}

	public double getYMax() {
		return yMax;
	}

	public boolean isInside(final double x, final double y) {
		return x >= xMin && x <= xMax && y >= yMin && y <= yMax;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Boundary boundary = (Boundary) o;
		return Double.compare(boundary.xMin, xMin) == 0 && Double.compare(boundary.yMin, yMin) == 0 && Double.compare(boundary.xMax, xMax) == 0 && Double.compare(boundary.yMax, yMax) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(xMin, yMin, xMax, yMax);
	}
}
