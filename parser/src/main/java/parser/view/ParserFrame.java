package parser.view;

import java.io.File;
import java.io.FileNotFoundException;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import parser.ProjectAnalyzer;

public class ParserFrame extends JFrame {
    public ParserFrame(final ProjectAnalyzer pa) {
		super("Async Parser");

		this.setLayout(new BorderLayout());

		final JPanel topBar = new JPanel();
		topBar.setLayout(new FlowLayout());
		this.add(topBar, BorderLayout.NORTH);

		JButton chooseFolder = new JButton("Choose Folder to analyze");
		chooseFolder.addActionListener(e -> {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.setDialogTitle("Choose Folder to analyze");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					pa.analyzeProject(file.getAbsolutePath());
				} catch (FileNotFoundException ignored) {}
			}
		});
		topBar.add(chooseFolder, BorderLayout.NORTH);

		final JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> {
			pa.stopAnalyze();
		});
		topBar.add(stopButton);

		this.add(new ProjectExplorer(pa.getEventBus()), BorderLayout.CENTER);

		this.add(new StatisticsPanel(pa.getEventBus()), BorderLayout.EAST);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
    }
}
