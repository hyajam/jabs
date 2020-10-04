package main.java.event;

import main.java.network.Network;
import main.java.simulator.Simulator;

public class TxGeneratorProcessRandomNode extends AbstractTxGeneratorProcess {
    public TxGeneratorProcessRandomNode(long averageTimeBetweenTxs) {
        super(averageTimeBetweenTxs);
    }

    @Override
    public void generate() {
        this.node = Network.getRandomNode();
        Simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
