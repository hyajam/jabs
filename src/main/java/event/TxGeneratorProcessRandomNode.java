package main.java.event;

import main.java.network.Network;
import main.java.random.Random;
import main.java.simulator.Simulator;

public class TxGeneratorProcessRandomNode extends AbstractTxGeneratorProcess {
    protected final Network network;

    public TxGeneratorProcessRandomNode(Simulator simulator, Network network, Random random, long averageTimeBetweenTxs) {
        super(simulator, random, averageTimeBetweenTxs);
        this.network = network;
    }

    @Override
    public void generate() {
        this.node = network.getRandomNode();
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
