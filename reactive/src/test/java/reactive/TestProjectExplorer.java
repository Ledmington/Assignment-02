package reactive;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import reactive.view.ProjectExplorer;

public class TestProjectExplorer {

    private class ProjectExplorerSon extends ProjectExplorer {
        public ProjectExplorerSon() {
            super();
        }

        public Map<String, DefaultMutableTreeNode> getNodes() {
            return nodes;
        }
    
        public Map<String, String> getParents() {
            return parents;
        }

        public void addAllNodes(final String fullName) {
            super.addAllNodes(fullName);
        }

        public void addNode(final String packageName, final String parentPackageName) {
            super.addNode(packageName, parentPackageName);
        }
    }
    
    private ProjectExplorerSon pe;

    @BeforeEach
    public void setup() {
        pe = new ProjectExplorerSon();
    }

    @Test
    public void rootIsAlwaysPresent() {
        assertTrue(pe.getNodes().containsKey("root"));
    }

    @Test
    public void canAddNodeAsSon() {
        pe.addNode("class", "root");
        assertTrue(pe.getNodes().containsKey("root"));
        assertTrue(pe.getNodes().containsKey("class"));
        assertEquals(pe.getParents().get("class"), "root");
    }

    @Test
    public void addingParentHierarchy() {
        pe.addAllNodes("reactive.class.method");

        assertTrue(pe.getNodes().containsKey("reactive"));
        assertTrue(pe.getNodes().containsKey("reactive.class"));
        assertTrue(pe.getNodes().containsKey("reactive.class.method"));

        assertEquals(pe.getParents().get("reactive"), "root");
        assertEquals(pe.getParents().get("reactive.class"), "reactive");
        assertEquals(pe.getParents().get("reactive.class.method"), "reactive.class");
    }

    @Test
    public void addingMethodsForInterfaceAndClass() {
        pe.addAllNodes("myInterface.method");
        pe.addAllNodes("myClass.method");

        assertTrue(pe.getNodes().containsKey("myClass"));
        assertTrue(pe.getNodes().containsKey("myInterface"));
        assertEquals(pe.getParents().get("myInterface.method"), "myInterface");
        assertEquals(pe.getParents().get("myClass.method"), "myClass");
    }
}
