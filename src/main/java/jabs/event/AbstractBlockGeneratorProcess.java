package jabs.event;

import jabs.node.nodes.MinerNode;
import jabs.random.Random;
import jabs.simulator.Simulator;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode miner;

    public AbstractBlockGeneratorProcess(Simulator simulator, Random random, long averageTimeBetweenBlocks) {
        super(simulator, random, averageTimeBetweenBlocks);
    }

    protected abstract void generate();
}
