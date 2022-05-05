package nbodies.view;

import nbodies.NBodies;
import nbodies.sim.data.SimulationData;

import javax.swing.*;
import java.awt.*;

public class VisualiserFrame extends JFrame {

    private final VisualiserPanel panel;
    private final JButton startButton;
    private final JButton stopButton;


    public VisualiserFrame(int w, int h) {
        setTitle("N-Bodies Simulation");
        setSize(w, h);
        setMinimumSize(new Dimension(w, h));
        setResizable(false);
        setFocusable(true);

        MovingArrowsListener listener = new MovingArrowsListener(w, h);
        addKeyListener(listener);

        setLayout(new BorderLayout());

        startButton = new JButton("START");
        stopButton = new JButton("STOP");

        if (NBodies.getSimulator().getData().isPaused()) {
            stopButton.setEnabled(false);
        } else {
            startButton.setEnabled(false);
        }

        startButton.setBounds(380, 10, 100, 20);
        stopButton.setBounds(500, 10, 100, 20);

        startButton.addActionListener((but) -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            SimulationData.setPaused(false);
            NBodies.getSimulator().getData().getPause().hitAndWaitAll();
        });
        stopButton.addActionListener((but) -> {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            SimulationData.setPaused(true);
        });

        getContentPane().add(startButton);
        getContentPane().add(stopButton);

        panel = new VisualiserPanel(w, h, listener);
        getContentPane().add(panel, BorderLayout.CENTER);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }

    public void display(final SimulationData data) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                if (data.isFinished()) {
                    startButton.setEnabled(false);
                    stopButton.setEnabled(false);
                }
                panel.display(data);
                repaint();
            });
        } catch (Exception ignored) {
        }
    }
}