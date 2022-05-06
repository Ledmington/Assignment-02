package parser.view;

import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import io.vertx.core.eventbus.EventBus;

public class StatisticsPanel extends JPanel {
    public StatisticsPanel(final EventBus bus) {
        super();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (int i=0; i<5; i++) {
            final JLabel label = new JLabel("uau: " + i);
            Border border = label.getBorder();
            Border margin = new EmptyBorder(10,10,10,10);
            label.setBorder(new CompoundBorder(border, margin));
            this.add(label);
        }
    }    
}
