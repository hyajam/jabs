package jabs.simulator.event;

import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractTxPoissonProcess extends AbstractPoissonProcess {
    protected Node node;

    public AbstractTxPoissonProcess(Simulator simulator, RandomnessEngine randomnessEngine, double averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
    }

    protected abstract void generate();
}
