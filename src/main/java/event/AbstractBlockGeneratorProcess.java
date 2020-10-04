package main.java.event;

import main.java.node.nodes.MinerNode;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode miner;

    public AbstractBlockGeneratorProcess(long averageTimeBetweenBlocks) {
        super(averageTimeBetweenBlocks);
    }

    protected abstract void generate();
}
