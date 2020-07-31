package main.java.event;

import main.java.node.nodes.Node;

public class NodeTxGeneratorProcess extends AbstractTxGeneratorProcess {
    public NodeTxGeneratorProcess(Node node, long averageTimeBetweenTxs, int maxTxs) {
        super(averageTimeBetweenTxs, maxTxs);
        this.node = node;
    }

    public NodeTxGeneratorProcess(Node node, long averageTimeBetweenTxs) {
        super(averageTimeBetweenTxs, -1);
        this.node = node;
    }

    @Override
    public void generate() {
        this.node.generateNewTransaction();
    }
}
