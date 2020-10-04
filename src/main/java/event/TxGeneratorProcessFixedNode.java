package main.java.event;

import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

public class TxGeneratorProcessFixedNode extends AbstractTxGeneratorProcess {
    public TxGeneratorProcessFixedNode(Node node, long averageTimeBetweenTxs) {
        super(averageTimeBetweenTxs);
        this.node = node;
    }

    @Override
    public void generate() {
        Simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
