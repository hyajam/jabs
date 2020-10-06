package main.java.network;

import main.java.data.Tx;
import main.java.data.bitcoin.BitcoinBlock;
import main.java.data.bitcoin.BitcoinTx;
import main.java.node.nodes.bitcoin.BitcoinNode;
import main.java.random.Random;
import main.java.simulator.Simulator;

public class BitcoinGlobalBlockchainNetwork extends GlobalBlockchainNetwork<BitcoinBlock, BitcoinTx> {
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

    private static final double[] BITCOIN_TRANSACTION_SIZE_DISTRIBUTION = {
            0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0587, 0.4874, 0.2303, 0.1339, 0.0518, 0.0197, 0.0089, 0.0040,
            0.0027, 0.0017, 0.0007, 0.0002
    };

    private static final long[] BITCOIN_TRANSACTION_SIZE_BINS = {
            87, 91, 96, 124, 166, 199, 263, 391, 647, 1159, 2183, 4231, 8327, 16519, 32940, 73141, 270464
    };

    @Override
    public Tx<BitcoinTx> sampleTransaction() {
        return new BitcoinTx((int) random.sampleDistributionWithBins(
                BITCOIN_TRANSACTION_SIZE_DISTRIBUTION, BITCOIN_TRANSACTION_SIZE_BINS));
    }
}
