package main.java.event;

import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

public abstract class AbstractTxGeneratorProcess extends AbstractGeneratorProcess {
    protected Node node;

    public AbstractTxGeneratorProcess(Simulator simulator, long averageTimeBetweenTxs) {
        super(simulator, averageTimeBetweenTxs);
    }

    protected abstract void generate();
}
