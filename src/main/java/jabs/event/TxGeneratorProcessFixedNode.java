package jabs.event;

import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class TxGeneratorProcessFixedNode extends AbstractTxGeneratorProcess {
    public TxGeneratorProcessFixedNode(Simulator simulator, RandomnessEngine randomnessEngine, Node node, long averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
        this.node = node;
    }

    @Override
    public void generate() {
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
