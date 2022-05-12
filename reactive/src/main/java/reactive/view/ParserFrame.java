package reactive.view;

import reactive.ProjectAnalyzer;
import reactive.ProjectElement;
import reactive.utils.Pair;

import javax.swing.*;

import io.reactivex.rxjava3.flowables.ConnectableFlowable;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.atomic.AtomicReference;

public class ParserFrame extends JFrame {

	private final ProjectExplorer explorer = new ProjectExplorer();
	private final StatisticsPanel stats = new StatisticsPanel();

    public ParserFrame(final ProjectAnalyzer pa) {
        super("Project parser");

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
				System.out.println("Opening \"" + file.getAbsolutePath() + "\"");
				try {
					final ConnectableFlowable<Pair<ProjectElement, String>> topic = pa.analyzeProject(file.getAbsolutePath());
					explorer.setTopic(topic);
					stats.setTopic(topic);
				} catch (FileNotFoundException ignored) {}
			}
		});
		topBar.add(chooseFolder, BorderLayout.NORTH);

		final JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(e -> pa.stopAnalyze());
		topBar.add(stopButton);

		this.add(explorer, BorderLayout.CENTER);

		this.add(stats, BorderLayout.EAST);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();
    }
}
