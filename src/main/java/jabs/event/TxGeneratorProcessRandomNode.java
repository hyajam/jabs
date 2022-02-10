package jabs.event;

import jabs.network.Network;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class TxGeneratorProcessRandomNode extends AbstractTxGeneratorProcess {
    protected final Network network;

    public TxGeneratorProcessRandomNode(Simulator simulator, Network network, RandomnessEngine randomnessEngine, long averageTimeBetweenTxs) {
        super(simulator, randomnessEngine, averageTimeBetweenTxs);
        this.network = network;
    }

    @Override
    public void generate() {
        this.node = network.getRandomNode();
        simulator.putEvent(new TxGenerationEvent(node), 0);
    }
}
