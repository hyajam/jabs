package jabs.network;

import jabs.node.nodes.Node;
import jabs.node.nodes.ethereum.EthereumMinerNode;
import jabs.node.nodes.ethereum.EthereumNode;
import jabs.random.Random;
import jabs.simulator.Simulator;

import static jabs.config.NetworkStats.ETHEREUM_HASH_POWER_DISTRIBUTION;
import static jabs.config.NetworkStats.ETHEREUM_HASH_POWER_DISTRIBUTION_BIN;

public class EthereumGlobalBlockchainNetwork extends GlobalBlockchainNetwork {
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
    public void populateNetwork(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners);
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

    @Override
    protected long sampleHashPower() {
        return random.sampleDistributionWithBins(ETHEREUM_HASH_POWER_DISTRIBUTION, ETHEREUM_HASH_POWER_DISTRIBUTION_BIN);
    }

}
