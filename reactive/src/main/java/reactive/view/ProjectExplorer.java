package reactive.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import reactive.ProjectElement;
import reactive.utils.Pair;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
    }

    public void setTopic(final ConnectableFlowable<Pair<ProjectElement, String>> publisher) {
        System.out.println("called setTopic on ProjectExplorer");
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
}
