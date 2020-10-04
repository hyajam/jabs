package main.java.event;

import main.java.network.Network;
import main.java.simulator.Simulator;

public class BlockGeneratorProcessRandomNode extends AbstractBlockGeneratorProcess {
    private final Network network;

    public BlockGeneratorProcessRandomNode(Network network, long averageTimeBetweenBlocks) {
        super(averageTimeBetweenBlocks);
        this.network = network;
    }

    @Override
    public void generate() {
        this.miner = Network.getRandomMinerByHashPower();
        Simulator.putEvent(new BlockGenerationEvent(miner), 0);
    }
}
