package parser.view;

import javax.swing.JTree;

import io.vertx.core.eventbus.EventBus;

public class ProjectExplorer extends JTree {
    
    private final EventBus bus;

    public ProjectExplorer(final EventBus bus) {
        super();
        this.bus = bus;
    }    
}
