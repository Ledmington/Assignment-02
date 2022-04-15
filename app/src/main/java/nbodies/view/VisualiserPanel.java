package nbodies.view;

import nbodies.Body;
import nbodies.Boundary;
import nbodies.P2d;
import nbodies.sim.data.SimulationData;
import nbodies.utils.stats.Statistics;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Locale;

public class VisualiserPanel extends JPanel {

	private final MovingArrowsListener listener;
	private final int dx;
	private final int dy;
	private SimulationData data;

	public VisualiserPanel(int w, int h, MovingArrowsListener listener) {
		setSize(w, h);
		dx = w / 2 - 20;
		dy = h / 2 - 20;
		this.listener = listener;
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		requestFocusInWindow();
	}

	private double getScale() {
		return listener.getScale();
	}

	private int getXCenter() {
		return listener.getXCenter();
	}

	private int getYCenter() {
		return listener.getYCenter();
	}

	public void paint(Graphics g) {
		if (data == null) return;

		ArrayList<Body> bodies = data.getBodies();
		Boundary bounds = data.getBounds();

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());

		int x0 = getXCoord(bounds.getXMin());
		int y0 = getYCoord(bounds.getYMin());

		int wd = getXCoord(bounds.getXMax()) - x0;
		int ht = y0 - getYCoord(bounds.getYMax());

		g2.drawRect(x0, y0 - ht, wd, ht);

		bodies.forEach(b -> {
			P2d p = b.getPos();
			int radius = (int) (10 * getScale());
			if (radius < 1) {
				radius = 1;
			}
			g2.drawOval(getXCoord(p.getX()), getYCoord(p.getY()), radius, radius);
		});

		double totalMod = bodies.stream().mapToDouble(b -> b.getVel().mod()).sum();
		Statistics spf = data.getFPSStats(); // seconds-per-frame

		String time = String.format("%.2f", data.getTime());
		g2.drawString("Bodies: " + bodies.size() + " - vt: " + time + " - nIter: " + data.getIteration(), 2, 10);
		g2.drawString(String.format("Total velocity: %.3e", totalMod), 2, 25);
		g2.drawString("Remaining time: " + data.getETA(), 2, 40);
		g2.drawString(String.format(Locale.US, "ms per frame -> min: %.3f, max: %.3f, avg: %.3f", spf.getMin(), spf.getMax(), spf.getAvg()), 2, 55);
		g2.drawString("(+/- to zoom, arrows to move around)", 2, 70);
	}

	private int getXCoord(double x) {
		return (int) (getXCenter() + x * dx * getScale());
	}

	private int getYCoord(double y) {
		return (int) (getYCenter() - y * dy * getScale());
	}

	public void display(final SimulationData data) {
		this.data = data;
	}
}