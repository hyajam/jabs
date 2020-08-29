package main.java.event;

import main.java.Main;
import main.java.network.Network;
import main.java.node.nodes.MinerNode;

public class RandomNodeBlockGeneratorProcess extends AbstractBlockGeneratorProcess {
    public RandomNodeBlockGeneratorProcess(long averageTimeBetweenBlocks) {
        super(averageTimeBetweenBlocks, -1);
    }

    public RandomNodeBlockGeneratorProcess(long averageTimeBetweenBlocks, int maxBlocks) {
        super(averageTimeBetweenBlocks, maxBlocks);
    }

    @Override
    public void generate() {
        long randomNodeHashPowerShare = (long) (Main.random.nextDouble() * Network.getTotalHashPower());
        for (MinerNode miner:Network.getAllMiners()) {
            randomNodeHashPowerShare -= miner.getHashPower();
            if (randomNodeHashPowerShare < 0) {
                this.node = miner;
                break;
            }
        }
        this.node.generateNewBlock();
    }
}
