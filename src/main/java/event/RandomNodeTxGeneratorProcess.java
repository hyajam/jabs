package main.java.event;

import main.java.network.Network;

public class RandomNodeTxGeneratorProcess extends AbstractTxGeneratorProcess {
    public RandomNodeTxGeneratorProcess(long averageTimeBetweenTxs) {
        super(averageTimeBetweenTxs, -1);
    }

    public RandomNodeTxGeneratorProcess(long averageTimeBetweenTxs, int maxTxs) {
        super(averageTimeBetweenTxs, maxTxs);
    }

    @Override
    public void generate() {
        Network.sampleNode().generateNewTransaction();
    }
}
