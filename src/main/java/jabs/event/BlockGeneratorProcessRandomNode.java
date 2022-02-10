package jabs.event;

import jabs.network.BlockchainNetwork;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BlockGeneratorProcessRandomNode extends AbstractBlockGeneratorProcess {
    private final BlockchainNetwork network;

    public BlockGeneratorProcessRandomNode(Simulator simulator, RandomnessEngine randomnessEngine, BlockchainNetwork network, long averageTimeBetweenBlocks) {
        super(simulator, randomnessEngine, averageTimeBetweenBlocks);
        this.network = network;
    }

    @Override
    public void generate() {
        this.miner = network.getRandomMinerByHashPower();
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
