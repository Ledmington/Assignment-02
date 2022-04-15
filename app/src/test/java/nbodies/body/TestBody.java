package nbodies.body;

import nbodies.Body;
import nbodies.Boundary;
import nbodies.P2d;
import nbodies.V2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestBody {

	private static final double EPS = 1e-6;

	private Boundary bounds;
	private Body b;

	// Must do this ugliness to please JPF
	private static <T> List<T> of(T... args) {
		List<T> l = new LinkedList<>();
		l.addAll(Arrays.asList(args));
		return l;
	}

	@BeforeEach
	public void setup() {
		bounds = new Boundary(-1, -1, 1, 1);
	}

	private Body body(final double x, final double y) {
		return new Body(new P2d(x, y), new V2d(0, 0), 1);
	}

	private boolean inside(final Body b) {
		return bounds.isInside(b.getPos().getX(), b.getPos().getY());
	}

	@Test
	public void bodyAlreadyInside() {
		b = body(0, 0);
		assertTrue(inside(b));
		b.checkAndSolveBoundaryCollision(bounds);
		assertTrue(inside(b));
	}

	@Test
	public void bodyTooMuchRight() {
		b = body(bounds.getXMax() + EPS, 0);
		assertFalse(inside(b));
		b.checkAndSolveBoundaryCollision(bounds);
		assertTrue(inside(b));
	}

	@Test
	public void bodyTooMuchLeft() {
		b = body(bounds.getXMin() - EPS, 0);
		assertFalse(inside(b));
		b.checkAndSolveBoundaryCollision(bounds);
		assertTrue(inside(b));
	}

	@Test
	public void bodyTooMuchUp() {
		b = body(0, bounds.getYMax() + EPS);
		assertFalse(inside(b));
		b.checkAndSolveBoundaryCollision(bounds);
		assertTrue(inside(b));
	}

	@Test
	public void bodyTooMuchDown() {
		b = body(0, bounds.getYMin() - EPS);
		assertFalse(inside(b));
		b.checkAndSolveBoundaryCollision(bounds);
		assertTrue(inside(b));
	}

	@Test
	public void cornersAreInside() {
		List<Body> corners = TestBody.of(
				body(bounds.getXMin(), bounds.getYMin()), // NW
				body(bounds.getXMax(), bounds.getYMin()), // NE
				body(bounds.getXMin(), bounds.getYMax()), // SW
				body(bounds.getXMax(), bounds.getYMax())  // SE
		);

		for (Body b : corners) {
			assertTrue(inside(b));
			final P2d oldPos = b.getPos();
			b.checkAndSolveBoundaryCollision(bounds);
			assertEquals(b.getPos(), oldPos); // if they are inside, they must not move
		}
	}

	@Test
	public void outFromCorners() {
		List<Body> corners = Stream.of(
						body(bounds.getXMin(), bounds.getYMin()), // NW
						body(bounds.getXMax(), bounds.getYMin()), // NE
						body(bounds.getXMin(), bounds.getYMax()), // SW
						body(bounds.getXMax(), bounds.getYMax())  // SE
				)
				.flatMap(b -> {
					final double x = b.getPos().getX();
					final double y = b.getPos().getY();
					return Stream.of(
							body(x - EPS, y - EPS), body(x, y - EPS), body(x + EPS, y - EPS),
							body(x - EPS, y), body(x, y), body(x + EPS, y),
							body(x - EPS, y + EPS), body(x, y + EPS), body(x + EPS, y + EPS)
					);
				})
				.filter(b -> !inside(b))
				.collect(Collectors.toList());

		for (Body b : corners) {
			assertFalse(inside(b)); // double check that they are inside
			final P2d oldPos = b.getPos();
			b.checkAndSolveBoundaryCollision(bounds);
			assertTrue(inside(b));
		}
	}
}
