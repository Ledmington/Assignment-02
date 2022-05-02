/*
 *   V2d.java
 *
 * Copyright 2000-2001-2002  aliCE team at deis.unibo.it
 *
 * This software is the proprietary information of deis.unibo.it
 * Use is subject to license terms.
 *
 */
package nbodies;

import java.util.Objects;

/**
 * 2-dimensional vector
 * objects are completely state-less
 */
public class V2d {

	public double x, y;

	public V2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public V2d(V2d v) {
		this.x = v.x;
		this.y = v.y;
	}

	public V2d(P2d from, P2d to) {
		this.x = to.getX() - from.getX();
		this.y = to.getY() - from.getY();
	}

	public V2d scalarMul(double k) {
		x *= k;
		y *= k;
		return this;
	}

	public void sum(V2d v) {
		x += v.x;
		y += v.y;
	}

	public double mod() {
		return Math.sqrt(x * x + y * y);
	}

	public V2d normalize() throws NullVectorException {
		double mod = mod();
		if (mod > 0) {
			x /= mod;
			y /= mod;
			return this;
		} else {
			throw new NullVectorException();
		}
	}

	public void change(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String toString() {
		return "V2d{" +
				"x=" + x +
				", y=" + y +
				'}';
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		V2d v2d = (V2d) o;
		return Double.compare(v2d.x, x) == 0 && Double.compare(v2d.y, y) == 0;
	}

	public int hashCode() {
		return Objects.hash(x, y);
	}
}
