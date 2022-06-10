package jabs.simulator.event;

import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class TxGenerationProcessSingleNode extends AbstractTxPoissonProcess {
    public TxGenerationProcessSingleNode(Simulator simulator, RandomnessEngine randomnessEngine, Node node, double averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
        this.node = node;
    }

    @Override
    public void generate() {
        node.generateNewTransaction();
    }
}
