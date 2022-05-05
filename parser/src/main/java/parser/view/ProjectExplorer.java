package parser.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;

import io.vertx.core.eventbus.EventBus;
import parser.ProjectElement;

public class ProjectExplorer extends JPanel {
    
    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;

    public ProjectExplorer(final EventBus bus) {
        super();

        this.setLayout(new BorderLayout());

        rootNode = new DefaultMutableTreeNode("Root");
        tree = new JTree(rootNode);
        this.add(new JScrollPane(tree), BorderLayout.CENTER);

        for (ProjectElement element : ProjectElement.values()) {
            bus.consumer(element.getName(), handler -> {
                final String nodeName = (String) handler.body();
                System.out.println("Found " + element.getName() + ": " + nodeName);
                SwingUtilities.invokeLater(() -> rootNode.add(new DefaultMutableTreeNode(nodeName)));
            });
        }
    }
}
