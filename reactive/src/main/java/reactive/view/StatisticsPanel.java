package reactive.view;

import reactive.ProjectElement;
import reactive.utils.Pair;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import io.reactivex.rxjava3.flowables.ConnectableFlowable;

import java.util.HashMap;
import java.util.Map;

public class StatisticsPanel extends JPanel {

    private final Map<String, Pair<JLabel, Integer>> count = new HashMap<>();

    public StatisticsPanel() {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final int defaultValue = 0;
        for (ProjectElement element : ProjectElement.values()) {
            final JLabel lbl = new JLabel(element.getName() + ": " + defaultValue);
            count.put(element.getName(), new Pair<>(lbl, defaultValue));
            Border border = lbl.getBorder();
            Border margin = new EmptyBorder(5, 10, 5, 10);
            lbl.setBorder(new CompoundBorder(border, margin));
            this.add(lbl);
        }
    }

    public void setTopic(final ConnectableFlowable<Pair<ProjectElement, String>> publisher) {
        // Registering handler
        publisher.subscribe(p -> {
            final Pair<JLabel, Integer> lblCount = count.get(p.first().getName());
            count.put(p.first().getName(), new Pair<>(lblCount.first(), lblCount.second()+1));
            SwingUtilities.invokeLater(() -> updateLabels());
        });

        // Flushing the "events"
        publisher.connect();
    }

    private void updateLabels() {
        for (String element : count.keySet()) {
            Pair<JLabel, Integer> counterLabel = count.get(element);
            counterLabel.first().setText(element + ": " + counterLabel.second());
        }
    }
}
