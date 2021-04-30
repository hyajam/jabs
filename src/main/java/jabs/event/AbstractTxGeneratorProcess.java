package jabs.event;

import jabs.node.nodes.Node;
import jabs.random.Random;
import jabs.simulator.Simulator;

public abstract class AbstractTxGeneratorProcess extends AbstractGeneratorProcess {
    protected Node node;

    public AbstractTxGeneratorProcess(Simulator simulator, Random random, long averageTimeBetweenTxs) {
        super(simulator, random, averageTimeBetweenTxs);
    }

    protected abstract void generate();
}
