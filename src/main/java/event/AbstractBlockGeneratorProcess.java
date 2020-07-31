package main.java.event;

import main.java.node.nodes.MinerNode;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode node;

    public AbstractBlockGeneratorProcess(long averageTimeBetweenBlocks, int maxNumOfBlocks) {
        super(averageTimeBetweenBlocks, maxNumOfBlocks);
    }

    protected abstract void generate();
}
