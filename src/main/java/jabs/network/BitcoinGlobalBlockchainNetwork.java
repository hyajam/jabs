package jabs.network;

import jabs.node.nodes.bitcoin.BitcoinNode;
import jabs.random.Random;
import jabs.simulator.Simulator;

public class BitcoinGlobalBlockchainNetwork extends GlobalBlockchainNetwork {
    private static final double[] BITCOIN_REGION_DISTRIBUTION_2019 = {0.3316, 0.4998, 0.0090, 0.1177, 0.0224, 0.0195};
    public static final double[] BITCOIN_MINER_REGION_DISTRIBUTION_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    public BitcoinGlobalBlockchainNetwork(Random random) {
        super(random);
    }

    @Override
    protected long sampleHashPower() {
        return 0; //FIXME: fix this.
    }

    public BitcoinNode createNewBitcoinNode(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleRegion();
        return new BitcoinNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region));
    }

    @Override
    public int sampleRegion() {
        return random.sampleFromDistribution(BITCOIN_REGION_DISTRIBUTION_2019);
    }

    @Override
    public int sampleMinerRegion() {
        return random.sampleFromDistribution(BITCOIN_MINER_REGION_DISTRIBUTION_2020);
    }

    @Override
    public void populateNetwork(Simulator simulator) {

    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {

    }
}
