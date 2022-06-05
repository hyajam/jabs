package jabs.simulator.event;

import jabs.network.node.nodes.MinerNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractBlockGeneratorProcess extends AbstractGeneratorProcess {
    protected MinerNode miner;

    public AbstractBlockGeneratorProcess(Simulator simulator, RandomnessEngine randomnessEngine, double averageTimeBetweenBlocks) {
        super(simulator, randomnessEngine, averageTimeBetweenBlocks);
    }

    protected abstract void generate();
}
