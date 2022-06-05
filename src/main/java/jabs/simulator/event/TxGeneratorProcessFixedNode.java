package jabs.simulator.event;

import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class TxGeneratorProcessFixedNode extends AbstractTxGeneratorProcess {
    public TxGeneratorProcessFixedNode(Simulator simulator, RandomnessEngine randomnessEngine, Node node, double averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
        this.node = node;
    }

    @Override
    public void generate() {
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
