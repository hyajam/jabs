package jabs.simulator.event;

import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BlockGeneratorProcessRandomNode extends AbstractBlockGeneratorProcess {
    private final GlobalProofOfWorkNetwork network;

    public BlockGeneratorProcessRandomNode(Simulator simulator, RandomnessEngine randomnessEngine, GlobalProofOfWorkNetwork network, double averageTimeBetweenBlocks) {
        super(simulator, randomnessEngine, averageTimeBetweenBlocks);
        this.network = network;
    }

    @Override
    public void generate() {
        this.miner = network.getRandomMinerByHashPower();
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
