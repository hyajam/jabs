package main.java.network;

import main.java.node.nodes.bitcoin.BitcoinNode;
import main.java.random.Random;
import main.java.simulator.Simulator;

public class BitcoinGlobalBlockchainNetwork extends GlobalBlockchainNetwork {
    private static final double[] BITCOIN_REGION_DISTRIBUTION_2019 = {0.3316, 0.4998, 0.0090, 0.1177, 0.0224, 0.0195};
    public static final double[] BITCOIN_MINER_REGION_DISTRIBUTION_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    public BitcoinNode createNewBitcoinNode(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleRegion();
        return new BitcoinNode(simulator, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region));
    }

    @Override
    public int sampleRegion(Random random) {
        return random.sampleFromDistribution(BITCOIN_REGION_DISTRIBUTION_2019);
    }

    @Override
    public int sampleMinerRegion(Random random) {
        return random.sampleFromDistribution(BITCOIN_MINER_REGION_DISTRIBUTION_2020);
    }

    @Override
    public void populateNetwork(Simulator simulator, Random random) {

    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {

    }
}
