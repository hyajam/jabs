package main.java.event;

import main.java.network.Network;

public class RandomNodeBlockGeneratorProcess extends AbstractBlockGeneratorProcess {
    public RandomNodeBlockGeneratorProcess(long averageTimeBetweenBlocks) {
        super(averageTimeBetweenBlocks, -1);
    }

    public RandomNodeBlockGeneratorProcess(long averageTimeBetweenBlocks, int maxBlocks) {
        super(averageTimeBetweenBlocks, maxBlocks);
    }

    @Override
    public void generate() {
        Network.sampleMinerByHashPower().generateNewBlock();
    }
}
