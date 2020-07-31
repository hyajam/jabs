package main.java.event;

import main.java.node.nodes.Node;

public abstract class AbstractTxGeneratorProcess extends AbstractGeneratorProcess {
    protected Node node;

    public AbstractTxGeneratorProcess(long averageTimeBetweenTxs, int maxNumOfTxs) {
        super(averageTimeBetweenTxs, maxNumOfTxs);
    }

    protected abstract void generate();
}
