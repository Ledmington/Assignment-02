package parser.view;

import java.util.Arrays;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import io.vertx.core.eventbus.EventBus;
import parser.ProjectElement;

public class ProjectExplorer extends JPanel {
    
    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;

    public ProjectExplorer(final EventBus bus) {
        super();

        rootNode = new DefaultMutableTreeNode("Root");
        tree = new JTree(rootNode);
        this.add(new JScrollPane(tree));

        for (ProjectElement element : ProjectElement.values()) {
            bus.consumer(element.getName(), handler -> {
                final String nodeName = (String) handler.body();
                System.out.println("Found " + element.getName() + ": " + nodeName);
                SwingUtilities.invokeLater(() -> rootNode.add(new DefaultMutableTreeNode(nodeName)));
            });
        }
    }
}
