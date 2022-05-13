package reactive.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import reactive.ProjectElement;
import reactive.utils.Pair;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
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
        // Registering handler
        publisher.subscribe(p -> {
            System.out.println("arrived " + p);
            if(p.second().equals("null")) return;
            //System.out.println("adding " + p);
            final String fullPackageName = p.second();
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
        
        // Flushing the "events"
        publisher.connect();
    }

    private void addAllNodes(final String fullName) {
        final String[] path = fullName.split("\\.");
        for (int i = 0; i < path.length - 1; i++) {
            addNode(path[i + 1], path[i]);
        }
    }

    private void addNode(final String packageName, final String parentPackageName) {
        if(nodes.containsKey(packageName)) return;
        System.out.println("Adding " + packageName + " under " + parentPackageName);
        
        parents.put(packageName, parentPackageName);
        final DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(packageName);
        nodes.put(packageName, packageNode);
        try {
            SwingUtilities.invokeAndWait(() -> nodes.get(parentPackageName).add(packageNode));
        } catch (Exception ignored) {}
    }
}
