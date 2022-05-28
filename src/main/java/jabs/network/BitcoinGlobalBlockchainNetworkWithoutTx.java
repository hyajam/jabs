package jabs.network;

import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BitcoinGlobalBlockchainNetworkWithoutTx extends BitcoinGlobalBlockchainNetwork {
    public BitcoinGlobalBlockchainNetworkWithoutTx(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createNewBitcoinMinerNodeWithoutTx(simulator, this, i));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createNewBitcoinNode(simulator, this, numMiners + i));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }
}
