package reactive.view;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.BorderLayout;

import io.reactivex.rxjava3.flowables.ConnectableFlowable;
import reactive.ProjectElement;
import reactive.utils.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ProjectExplorer extends JPanel {

    private final JTree tree;
    private final DefaultMutableTreeNode rootNode;
    protected final Map<String, DefaultMutableTreeNode> nodes = new HashMap<>();
    protected final Map<String, String> parents = new HashMap<>();

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
            SwingUtilities.invokeAndWait(() -> nodes.get(parentPackageName).add(packageNode));
        } catch (Exception ignored) {}
    }
}
