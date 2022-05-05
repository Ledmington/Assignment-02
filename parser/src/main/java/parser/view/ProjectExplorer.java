package parser.view;

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
        this.add(tree);
        //this.add(new JScrollPane(tree));

        bus.consumer(ProjectElement.PACKAGE.getName(), handler -> {
            final String packageName = (String) handler.body();
            System.out.println("Found package: " + packageName);
            SwingUtilities.invokeLater(() -> {
                rootNode.add(new DefaultMutableTreeNode(packageName));
            });
        });

        bus.consumer(ProjectElement.CLASS.getName(), handler -> {
            final String className = (String) handler.body();
            System.out.println("Found class: " + className);
            SwingUtilities.invokeLater(() -> {
                rootNode.add(new DefaultMutableTreeNode(className));
            });
        });
    }
}
