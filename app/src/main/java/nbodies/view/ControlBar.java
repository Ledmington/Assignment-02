package nbodies.view;

import nbodies.NBodies;

import javax.swing.*;

public class ControlBar extends JPanel {

	private final JButton startButton;
	private final JButton stopButton;
	private final JLabel statusLabel;
	private boolean neverStarted = true;

	public ControlBar() {
		startButton = new JButton("Start");
		startButton.addActionListener(e -> {
			if (neverStarted) {
				NBodies.getSimulator().execute();
				neverStarted = false;
			} else {
				NBodies.getSimulator().start();
			}
		});
		add(startButton);

		stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> NBodies.getSimulator().stop());
		add(stopButton);

		statusLabel = new JLabel("Status: ");
		add(statusLabel);

		Thread statusUpdaterThread = new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException ignored) {
				}
				checkAndUpdateStatus();
			}
		});
		statusUpdaterThread.start();
	}

	private void checkAndUpdateStatus() {
		if (NBodies.getSimulator() == null) return;

		if (NBodies.getSimulator().isRunning()) {
			statusLabel.setText("Status: running");
			stopButton.setEnabled(true);
			startButton.setEnabled(false);
		} else {
			statusLabel.setText("Status: stopped");
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
		}
	}
}
