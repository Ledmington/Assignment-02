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
import java.util.concurrent.atomic.AtomicReference;

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
        publisher.subscribe(p -> {
            System.out.println("devi pagare le tasseh: " + p.first().getName());
        });
    }

    private void updateLabels() {
        for (String element : count.keySet()) {
            Pair<JLabel, Integer> counterLabel = count.get(element);
            counterLabel.first().setText(element + ": " + counterLabel.second());
        }
    }
}
