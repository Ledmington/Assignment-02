package parser.view;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.eventbus.EventBus;
import parser.ProjectElement;

public class ProjectExplorer extends JPanel {
    
    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    protected final Map<String, DefaultMutableTreeNode> nodes = new HashMap<>();
    protected final Map<String, String> parents = new HashMap<>();

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
                addNode(fullPackageName, "root");
            } else {
                // indirect son of root
                // adding all parents before the son
                addAllNodes(fullPackageName);
            }
        });

        Arrays.stream(ProjectElement.values())
            .filter(pe -> pe != ProjectElement.PACKAGE)
            .forEach(pe -> {
                bus.consumer(pe.getName(), handler -> {
                    final String fullName = (String) handler.body();
                    addAllNodes(fullName);
                });
            });
    }

    protected void addAllNodes(final String fullName) {
        if(nodes.containsKey(fullName)) return;

        final List<String> splitPath = Arrays.asList(fullName.split("\\."));
        addNode(splitPath.get(0), "root");
        for (int i = 1; i < splitPath.size(); i++) {
            addNode(
                String.join(".", splitPath.subList(0, i+1)),
                String.join(".", splitPath.subList(0, i))
                );
        }
    }

    protected void addNode(final String packageName, final String parentPackageName) {
        if(nodes.containsKey(packageName)) return;

        parents.put(packageName, parentPackageName);
        final String[] splitPath = packageName.split("\\.");
        final DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(splitPath[splitPath.length-1]);
        nodes.put(packageName, packageNode);
        try {
            SwingUtilities.invokeLater(() -> nodes.get(parentPackageName).add(packageNode));
        } catch (Exception ignored) {}
    }
}
