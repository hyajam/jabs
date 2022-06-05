package jabs.network.networks;

import jabs.network.networks.stats.*;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNodeWithoutTx;
import jabs.network.node.nodes.bitcoin.BitcoinNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BitcoinGlobalProofOfWorkNetwork<R extends Enum<R>> extends GlobalProofOfWorkNetwork<R> {

    public BitcoinGlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine,
                                           ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    public BitcoinNode createNewBitcoinNode(Simulator simulator, Network<R> network, int nodeID) {
        R region = this.sampleRegion();
        return new BitcoinNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region));
    }

    public BitcoinMinerNode createNewBitcoinMinerNode(Simulator simulator, Network<R> network, int nodeID) {
        R region = this.sampleRegion();
        return new BitcoinMinerNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower());
    }

    public BitcoinMinerNode createNewBitcoinMinerNodeWithoutTx(Simulator simulator, Network<R> network, int nodeID) {
        R region = this.sampleRegion();
        return new BitcoinMinerNodeWithoutTx(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower());
    }

    @Override
    public void populateNetwork(Simulator simulator) {
        this.populateNetwork(simulator, minerDistribution.totalNumberOfMiners(), nodeDistribution.totalNumberOfNodes());
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners);
    }

    public void populateNetworkWithoutTx(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetworkWithoutTx(simulator, numMiners, numNodes-numMiners);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createNewBitcoinMinerNode(simulator, this, i));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createNewBitcoinNode(simulator, this, numMiners + i));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    public void populateNetworkWithoutTx(Simulator simulator, int numMiners, int numNonMiners) {
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
