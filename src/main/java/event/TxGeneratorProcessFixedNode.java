package main.java.event;

import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

public class TxGeneratorProcessFixedNode extends AbstractTxGeneratorProcess {
    public TxGeneratorProcessFixedNode(Simulator simulator, Node node, long averageTimeBetweenTxs) {
        super(simulator, averageTimeBetweenTxs);
        this.node = node;
    }

    @Override
    public void generate() {
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
