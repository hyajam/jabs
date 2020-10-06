package main.java.event;

import main.java.network.Network;
import main.java.simulator.Simulator;

public class BlockGeneratorProcessRandomNode extends AbstractBlockGeneratorProcess {
    private final Network network;

    public BlockGeneratorProcessRandomNode(Simulator simulator, Network network, long averageTimeBetweenBlocks) {
        super(simulator, averageTimeBetweenBlocks);
        this.network = network;
    }

    @Override
    public void generate() {
        this.miner = network.getRandomMinerByHashPower();
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
