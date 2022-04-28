package parser.view;

import parser.ProjectAnalyzer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class ParserFrame extends JFrame {
	public ParserFrame(final ProjectAnalyzer pa) {
		super("Project parser");

		this.setLayout(new BorderLayout());

		JButton chooseFolder = new JButton("Choose Folder to analyze");
		chooseFolder.addActionListener(e -> {
			final JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("."));
			fc.setDialogTitle("patatone");
			fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				System.out.println("Opening \"" + file.getAbsolutePath() + "\"");
				try {
					pa.analyzeProject(file.getAbsolutePath(), f -> {});
				} catch (FileNotFoundException ignored) {}
			}
		});
		this.add(chooseFolder, BorderLayout.CENTER);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.pack();
	}
}
