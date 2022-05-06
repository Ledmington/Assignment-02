package parser.view;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

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
                addNode(fullPackageName, "root");
            } else {
                // indirect son of root
                // adding all parents before the son
                addAllNodes(fullPackageName);
            }
        });

        bus.consumer(ProjectElement.CLASS.getName(), handler -> {
            final String fullClassName = (String) handler.body();
            addAllNodes(fullClassName);
        });

        bus.consumer(ProjectElement.INTERFACE.getName(), handler -> {
            final String fullInterfaceName = (String) handler.body();
            addAllNodes(fullInterfaceName);
        });

        bus.consumer(ProjectElement.FIELD.getName(), handler -> {
            final String fullFieldName = (String) handler.body();
            addAllNodes(fullFieldName);
        });

        bus.consumer(ProjectElement.METHOD.getName(), handler -> {
            final String fullMethodName = (String) handler.body();
            addAllNodes(fullMethodName);
        });

        bus.consumer(ProjectElement.METHOD_SIGNATURE.getName(), handler -> {
            final String fullMethodSignatureName = (String) handler.body();
            addAllNodes(fullMethodSignatureName);
        });

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        Icon closedIcon = new ImageIcon("src/main/res/img/package.png");
        Icon openIcon = new ImageIcon("src/main/res/img/package.png");
        Icon leafIcon = new ImageIcon("src/main/res/img/package.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);    
    }

    private void addAllNodes(final String fullName) {
        final String[] path = fullName.split("\\.");
        for (int i = 0; i < path.length - 1; i++) {
            addNode(path[i+1], path[i]);
        }
    }

    private void addNode(final String packageName, final String parentPackageName) {
        if (!nodes.containsKey(packageName)) {
            final String parentPackage = parentPackageName;
            parents.put(packageName, parentPackage);
            final DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(packageName);
            nodes.put(packageName, packageNode);
            SwingUtilities.invokeLater(() -> nodes.get(parentPackage).add(packageNode));
        }
    }
}
