package jabs.simulator.event;

import jabs.network.node.nodes.MinerNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BlockMiningProcess extends AbstractPoissonProcess {
    protected final MinerNode miner;

    public BlockMiningProcess(Simulator simulator, RandomnessEngine randomnessEngine, double averageTimeBetweenBlocks,
                              MinerNode miner) {
        super(simulator, randomnessEngine, averageTimeBetweenBlocks);
        this.miner = miner;
    }

    @Override
    public void generate() {
        miner.generateNewBlock();
    }

    public MinerNode getMiner() {
        return this.miner;
    }
}
