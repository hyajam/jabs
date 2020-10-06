package main.java.event;

import main.java.node.nodes.Node;
import main.java.random.Random;
import main.java.simulator.Simulator;

public abstract class AbstractTxGeneratorProcess extends AbstractGeneratorProcess {
    protected Node node;

    public AbstractTxGeneratorProcess(Simulator simulator, Random random, long averageTimeBetweenTxs) {
        super(simulator, random, averageTimeBetweenTxs);
    }

    protected abstract void generate();
}
