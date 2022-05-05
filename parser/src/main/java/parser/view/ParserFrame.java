package parser.view;

import java.io.File;
import java.io.FileNotFoundException;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import parser.ProjectAnalyzer;

public class ParserFrame extends JFrame {
    public ParserFrame(final ProjectAnalyzer pa) {
		super("Project parser");

		this.setLayout(new BorderLayout());

		JButton chooseFolder = new JButton("Choose Folder to analyze");
		chooseFolder.addActionListener(e -> {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.setDialogTitle("Choose Folder to analyze");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				System.out.println("Opening \"" + file.getAbsolutePath() + "\"");
				try {
					pa.analyzeProject(file.getAbsolutePath());
				} catch (FileNotFoundException ignored) {}
			}
		});
		this.add(chooseFolder, BorderLayout.NORTH);

		this.add(new ProjectExplorer(pa.getEventBus()), BorderLayout.CENTER);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
    }
}
