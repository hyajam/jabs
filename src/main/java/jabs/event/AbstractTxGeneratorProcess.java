package jabs.event;

import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractTxGeneratorProcess extends AbstractGeneratorProcess {
    protected Node node;

    public AbstractTxGeneratorProcess(Simulator simulator, RandomnessEngine randomnessEngine, long averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
    }

    protected abstract void generate();
}
