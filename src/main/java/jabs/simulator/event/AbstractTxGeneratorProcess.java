package jabs.simulator.event;

import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractTxGeneratorProcess extends AbstractGeneratorProcess {
    protected Node node;

    public AbstractTxGeneratorProcess(Simulator simulator, RandomnessEngine randomnessEngine, double averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
    }

    protected abstract void generate();
}
