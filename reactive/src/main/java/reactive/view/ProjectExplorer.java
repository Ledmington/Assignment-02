package reactive.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ProjectExplorer extends JPanel {

    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    private final Map<String, DefaultMutableTreeNode> nodes = new HashMap<>();
    private final Map<String, String> parents = new HashMap<>();

    public ProjectExplorer() {
        super();

        this.setLayout(new BorderLayout());

        rootNode = new DefaultMutableTreeNode("root");
        nodes.put("root", rootNode);
        tree = new JTree(rootNode);
        this.add(new JScrollPane(tree), BorderLayout.CENTER);
/*
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
            updateGUI();
        });

        Arrays.stream(ProjectElement.values())
            .filter(pe -> pe != ProjectElement.PACKAGE)
            .forEach(pe -> {
                bus.consumer(pe.getName(), handler -> {
                    final String fullName = (String) handler.body();
                    addAllNodes(fullName);
                    //SwingUtilities.updateComponentTreeUI(this);
                });
            });
        */
        /*DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        Icon closedIcon = new ImageIcon("src/main/res/img/box.png");
        Icon openIcon = new ImageIcon("src/main/res/img/open-box.png");
        Icon leafIcon = new ImageIcon("src/main/res/img/object.png");
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);*/
    }

    private void addAllNodes(final String fullName) {
        final String[] path = fullName.split("\\.");
        for (int i = 0; i < path.length - 1; i++) {
            addNode(path[i + 1], path[i]);
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

    private void updateGUI() {
        SwingUtilities.invokeLater(() -> {
            this.invalidate();
            this.validate();
            this.repaint();
        });
    }
}
