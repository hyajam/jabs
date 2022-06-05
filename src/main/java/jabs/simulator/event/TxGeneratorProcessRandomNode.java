package jabs.simulator.event;

import jabs.network.networks.Network;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class TxGeneratorProcessRandomNode extends AbstractTxGeneratorProcess {
    protected final Network network;

    public TxGeneratorProcessRandomNode(Simulator simulator, Network network, RandomnessEngine randomnessEngine, double averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
        this.network = network;
    }

    @Override
    public void generate() {
        this.node = network.getRandomNode();
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
