package main.java.event;

import main.java.node.nodes.MinerNode;
import main.java.random.Random;
import main.java.simulator.Simulator;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode miner;

    public AbstractBlockGeneratorProcess(Simulator simulator, Random random, long averageTimeBetweenBlocks) {
        super(simulator, random, averageTimeBetweenBlocks);
    }

    protected abstract void generate();
}
