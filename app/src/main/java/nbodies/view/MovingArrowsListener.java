package nbodies.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MovingArrowsListener implements KeyListener {

	private static final int MOVEMENT_SPEED = 3;

	private double scale = 0.1;
	private int x;
	private int y;

	public MovingArrowsListener(int w, int h) {
		x = w / 2 - 20;
		y = h / 2 - 20;
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			// zoom
			case KeyEvent.VK_PLUS:
				scale *= 1.1;
			case KeyEvent.VK_MINUS:
				scale *= 0.9;

			// move
			case KeyEvent.VK_UP:
				y -= MOVEMENT_SPEED;
			case KeyEvent.VK_DOWN:
				y += MOVEMENT_SPEED;
			case KeyEvent.VK_LEFT:
				x -= MOVEMENT_SPEED;
			case KeyEvent.VK_RIGHT:
				x += MOVEMENT_SPEED;
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public double getScale() {
		return scale;
	}

	public int getXCenter() {
		return x;
	}

	public int getYCenter() {
		return y;
	}
}
