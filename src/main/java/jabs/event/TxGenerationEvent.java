package jabs.event;

import jabs.node.nodes.Node;

public class TxGenerationEvent implements Event {
    private final Node node;

    public TxGenerationEvent(Node node) {
        this.node = node;
    }

    @Override
    public void execute() {
        node.generateNewTransaction();
    }
}
