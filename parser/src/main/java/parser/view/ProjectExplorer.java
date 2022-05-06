package parser.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.eventbus.EventBus;
import parser.ProjectElement;

public class ProjectExplorer extends JPanel {
    
    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    private final Map<String, DefaultMutableTreeNode> nodes = new HashMap<>();
    private final Map<String, String> parents = new HashMap<>();

    public ProjectExplorer(final EventBus bus) {
        super();

        this.setLayout(new BorderLayout());

        rootNode = new DefaultMutableTreeNode("root");
        nodes.put("root", rootNode);
        tree = new JTree(rootNode);
        this.add(new JScrollPane(tree), BorderLayout.CENTER);

        bus.consumer(ProjectElement.PACKAGE.getName(), handler -> {
            if (handler.body() == null) return;
            final String fullPackageName = (String) handler.body();
            final String[] packagePath = fullPackageName.split("\\.");
            if (packagePath.length == 1) {
                // direct son of root
                if (!nodes.containsKey(fullPackageName)) {
                    parents.put(fullPackageName, "root");
                    final DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(fullPackageName);
                    nodes.put(fullPackageName, packageNode);
                    SwingUtilities.invokeLater(() -> nodes.get("root").add(packageNode));
                }
            } else {
                // indirect son of root
                // adding all parents before the son
                for (int i=0; i<packagePath.length-1; i++) {
                    final String packageName = packagePath[i+1];
                    if (!nodes.containsKey(packageName)) {
                        final String parentPackage = packagePath[i];
                        parents.put(packageName, parentPackage);
                        final DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(packageName);
                        nodes.put(packageName, packageNode);
                        SwingUtilities.invokeLater(() -> nodes.get(parentPackage).add(packageNode));
                    }
                }
            }
        });
        /*
        for (ProjectElement element : ProjectElement.values()) {
            bus.consumer(element.getName(), handler -> {
                final String nodeName = (String) handler.body();
                System.out.println("Found " + element.getName() + ": " + nodeName);
                final DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeName);
                nodes.put(nodeName, node);
                SwingUtilities.invokeLater(() -> rootNode.add(node));
            });
        }*/
    }
}
