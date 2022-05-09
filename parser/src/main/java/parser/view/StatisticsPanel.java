package parser.view;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;

import io.vertx.core.eventbus.EventBus;
import parser.ProjectElement;
import parser.utils.Pair;

public class StatisticsPanel extends JPanel {

    private final Map<String, Pair<JLabel, Integer>> count = new HashMap<>();

    public StatisticsPanel(final EventBus bus) {
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
            bus.consumer(element.getName(), handler -> {
                Pair<JLabel, Integer> counterLabel = count.get(element.getName());
                count.put(element.getName(), new Pair<>(counterLabel.first(), counterLabel.second()+1));
                SwingUtilities.invokeLater(() -> updateLabels());
            });
        }
    }

    private void updateLabels() {
        for (String element : count.keySet()) {
            Pair<JLabel, Integer> counterLabel = count.get(element);
            counterLabel.first().setText(element + ": " + counterLabel.second());
        }
    }
}
