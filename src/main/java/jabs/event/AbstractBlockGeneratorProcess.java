package jabs.event;

import jabs.node.nodes.MinerNode;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode miner;

    public AbstractBlockGeneratorProcess(Simulator simulator, RandomnessEngine randomnessEngine, double averageTimeBetweenBlocks) {
        super(simulator, randomnessEngine, averageTimeBetweenBlocks);
    }

    protected abstract void generate();
}
