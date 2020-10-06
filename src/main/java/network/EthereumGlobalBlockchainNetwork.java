package main.java.network;

import main.java.data.ethereum.EthereumTx;
import main.java.node.nodes.Node;
import main.java.node.nodes.ethereum.EthereumMinerNode;
import main.java.node.nodes.ethereum.EthereumNode;
import main.java.random.Random;
import main.java.simulator.Simulator;

import static main.java.config.NetworkStats.ETHEREUM_HASH_POWER_DISTRIBUTION;
import static main.java.config.NetworkStats.ETHEREUM_HASH_POWER_DISTRIBUTION_BIN;

public class EthereumGlobalBlockchainNetwork extends GlobalBlockchainNetwork {
    private static final double[] ETHEREUM_TRANSACTION_SIZE_DISTRIBUTION = {
            0.33566250, 0.00029251, 0.03196772, 0.00135259, 0.00431051, 0.04577845, 0.46076570, 0.05192406, 0.03867314,
            0.0175110, 0.00219022, 0.00126404, 0.00041145
    };

    private static final long[] ETHEREUM_TRANSACTION_GAS_BINS = {
            21001, 21003, 21007, 21015, 21031, 21063, 21127, 21255, 21511, 22023, 23048, 25097, 29204, 37401, 53785,
            86553, 152114, 283357, 548945, 1141405, 12475587
    };

    private static final double[] BITCOIN_TRANSACTION_GAS_DISTRIBUTION = {
            0.000016894, 0.000069723, 0.000020709, 0.000260152, 0.001366569, 0.000187092, 0.000253752, 0.000358685,
            0.002723765, 0.001816119, 0.003954819, 0.012228619, 0.023134672, 0.139374064, 0.146851576, 0.241159595,
            0.110655111, 0.060103259, 0.032087937, 0.023120530
    };

    private static final long[] ETHEREUM_TRANSACTION_SIZE_BINS = {
            214, 216, 220, 228, 244, 276, 340, 470, 729, 1298, 2639, 5084, 12630, 296985
    };

    private static final double[] ETHEREUM_REGION_DISTRIBUTION_2020 = {0.3503, 0.3563, 0.0100, 0.2358, 0.0414, 0.0062};
    public static final double[] ETHEREUM_MINER_REGION_DISTRIBUTION_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    public static final int ETHEREUM_NUM_NODES_2020 = 6203;
    public static final int ETHEREUM_NUM_MINERS_2020 = 56;

    public EthereumGlobalBlockchainNetwork(Random random) {
        super(random);
    }

    @Override
    public void populateNetwork(Simulator simulator) {
        populateNetwork(simulator, ETHEREUM_NUM_MINERS_2020, ETHEREUM_NUM_NODES_2020);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createNewEthereumMiner(simulator, this, i));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createNewEthereumNode(simulator, this, numMiners + i));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    public EthereumNode createNewEthereumNode(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleRegion();
        return new EthereumNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region));
    }

    public EthereumMinerNode createNewEthereumMiner(Simulator simulator, Network network, int nodeID) {
        int region = this.sampleMinerRegion();
        return new EthereumMinerNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower());
    }

    @Override
    public int sampleRegion() {
        return random.sampleFromDistribution(ETHEREUM_REGION_DISTRIBUTION_2020);
    }

    @Override
    public int sampleMinerRegion() {
        return random.sampleFromDistribution(ETHEREUM_MINER_REGION_DISTRIBUTION_2020);
    }

    protected long sampleHashPower() {
        return random.sampleDistributionWithBins(ETHEREUM_HASH_POWER_DISTRIBUTION, ETHEREUM_HASH_POWER_DISTRIBUTION_BIN);
    }

    public EthereumTx sampleEthereumTransaction() {
        return new EthereumTx(
                (int) random.sampleDistributionWithBins(
                        ETHEREUM_TRANSACTION_SIZE_DISTRIBUTION, ETHEREUM_TRANSACTION_SIZE_BINS),
                (int) random.sampleDistributionWithBins(
                        BITCOIN_TRANSACTION_GAS_DISTRIBUTION, ETHEREUM_TRANSACTION_GAS_BINS));
    }
}
