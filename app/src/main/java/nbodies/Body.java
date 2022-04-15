package nbodies;

import java.util.Objects;

/*
 * This class represents a body
 *
 */
public class Body {

	public static final double REPULSIVE_CONST = 0.01;
	public static final double FRICTION_CONST = 1;

	private final P2d pos;
	private final V2d vel;
	private final double mass;

	public Body(P2d pos, V2d vel, double mass) {
		this.pos = pos;
		this.vel = vel;
		this.mass = mass;
	}

	public Body(final Body b) {
		this.pos = new P2d(b.pos);
		this.vel = new V2d(b.vel);
		this.mass = b.mass;
	}

	public double getMass() {
		return mass;
	}

	public P2d getPos() {
		return pos;
	}

	public V2d getVel() {
		return vel;
	}

	/**
	 * Update the position, according to current velocity
	 *
	 * @param dt time elapsed
	 */
	public void updatePos(double dt) {
		pos.sum(new V2d(vel).scalarMul(dt));
	}

	/**
	 * Update the velocity, given the instant acceleration
	 *
	 * @param acc instant acceleration
	 * @param dt  time elapsed
	 */
	public void updateVelocity(V2d acc, double dt) {
		vel.sum(new V2d(acc).scalarMul(dt));
	}

	/**
	 * Change the velocity
	 */
	public void changeVel(double vx, double vy) {
		vel.change(vx, vy);
	}

	/**
	 * Computes the distance from the specified body
	 */
	public double getDistanceFrom(Body b) {
		double dx = pos.getX() - b.getPos().getX();
		double dy = pos.getY() - b.getPos().getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Compute the repulsive force exerted by another body
	 */
	public V2d computeRepulsiveForceBy(Body b) throws InfiniteForceException {
		double dist = getDistanceFrom(b);
		if (dist > 0) {
			try {
				return new V2d(b.getPos(), pos)
						.normalize()
						.scalarMul(b.getMass() * REPULSIVE_CONST / (dist * dist));
			} catch (Exception ex) {
				throw new InfiniteForceException();
			}
		} else {
			throw new InfiniteForceException();
		}
	}

	/**
	 * Compute current friction force, given the current velocity
	 */
	public V2d getCurrentFrictionForce() {
		return new V2d(vel).scalarMul(-FRICTION_CONST);
	}

	/**
	 * Check if there are collisions with the boundary and update the
	 * position and velocity accordingly
	 */
	public void checkAndSolveBoundaryCollision(Boundary bounds) {
		double x = pos.getX();
		double y = pos.getY();

		if (x > bounds.getXMax()) {
			pos.setX(bounds.getXMax());
			vel.change(-vel.getX(), vel.getY());
		} else if (x < bounds.getXMin()) {
			pos.setX(bounds.getXMin());
			vel.change(-vel.getX(), vel.getY());
		}

		if (y > bounds.getYMax()) {
			pos.setY(bounds.getYMax());
			vel.change(vel.getX(), -vel.getY());
		} else if (y < bounds.getYMin()) {
			pos.setY(bounds.getYMin());
			vel.change(vel.getX(), -vel.getY());
		}
	}

	public String toString() {
		return "Body{" +
				"pos=" + pos +
				", vel=" + vel +
				", mass=" + mass +
				'}';
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Body other = (Body) o;
		return Double.compare(this.mass, other.mass) == 0 &&
				this.pos.equals(other.pos) &&
				this.vel.equals(other.vel);
	}

	public int hashCode() {
		return Objects.hash(pos, vel, mass);
	}
}
