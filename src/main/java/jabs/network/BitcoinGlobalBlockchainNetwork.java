package jabs.network;

import jabs.node.nodes.Node;
import jabs.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.node.nodes.bitcoin.BitcoinMinerNodeWithoutTx;
import jabs.node.nodes.bitcoin.BitcoinNode;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import static jabs.config.NetworkStats.*;

public class BitcoinGlobalBlockchainNetwork extends GlobalBlockchainNetwork {
    private static final double[] BITCOIN_REGION_DISTRIBUTION_2019 = {0.3316, 0.4998, 0.0090, 0.1177, 0.0224, 0.0195};
    public static final double[] BITCOIN_MINER_REGION_DISTRIBUTION_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    public BitcoinGlobalBlockchainNetwork(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    @Override
    protected long sampleHashPower() {
        return randomnessEngine.sampleDistributionWithBins(BITCOIN_HASH_POWER_DISTRIBUTION, BITCOIN_HASH_POWER_DISTRIBUTION_BIN);
    }

    public BitcoinNode createNewBitcoinNode(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleRegion();
        return new BitcoinNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region));
    }

    public BitcoinMinerNode createNewBitcoinMinerNode(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleRegion();
        return new BitcoinMinerNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower());
    }

    public BitcoinMinerNode createNewBitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleRegion();
        return new BitcoinMinerNodeWithoutTx(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower());
    }

    @Override
    public int sampleRegion() {
        return randomnessEngine.sampleFromDistribution(BITCOIN_REGION_DISTRIBUTION_2019);
    }

    @Override
    public int sampleMinerRegion() {
        return randomnessEngine.sampleFromDistribution(BITCOIN_MINER_REGION_DISTRIBUTION_2020);
    }

    @Override
    public void populateNetwork(Simulator simulator) {
        this.populateNetwork(simulator, 6000);
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
