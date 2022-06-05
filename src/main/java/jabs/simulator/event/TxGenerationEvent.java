package jabs.simulator.event;

import jabs.network.node.nodes.Node;

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
