package main.java.event;

import main.java.network.BlockchainNetwork;
import main.java.random.Random;
import main.java.simulator.Simulator;

public class BlockGeneratorProcessRandomNode extends AbstractBlockGeneratorProcess {
    private final BlockchainNetwork network;

    public BlockGeneratorProcessRandomNode(Simulator simulator, Random random, BlockchainNetwork network, long averageTimeBetweenBlocks) {
        super(simulator, random, averageTimeBetweenBlocks);
        this.network = network;
    }

    @Override
    public void generate() {
        this.miner = network.getRandomMinerByHashPower();
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
