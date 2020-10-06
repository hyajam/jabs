package main.java.event;

import main.java.node.nodes.MinerNode;
import main.java.simulator.Simulator;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode miner;

    public AbstractBlockGeneratorProcess(Simulator simulator, long averageTimeBetweenBlocks) {
        super(simulator, averageTimeBetweenBlocks);
    }

    protected abstract void generate();
}
