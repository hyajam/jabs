package jabs.event;

import jabs.node.nodes.Node;
import jabs.random.Random;
import jabs.simulator.Simulator;

public class TxGeneratorProcessFixedNode extends AbstractTxGeneratorProcess {
    public TxGeneratorProcessFixedNode(Simulator simulator, Random random, Node node, long averageTimeBetweenTxs) {
        super(simulator, random, averageTimeBetweenTxs);
        this.node = node;
    }

    @Override
    public void generate() {
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
