package main.java.node;

import main.java.node.nodes.bitcoin.BitcoinNode;
import main.java.node.nodes.ethereum.*;
import main.java.node.nodes.pbft.PBFTNode;

import static main.java.config.NetworkStats.*;
import static main.java.random.Random.sampleDistributionWithBins;
import static main.java.random.Random.sampleFromDistribution;

public final class NodeFactory {
    public static EthereumNode createNewEthereumNode(int nodeID) {
        return new EthereumNode(nodeID, sampleRegionEthereum());
    }

    public static EthereumMinerNode createNewEthereumMiner(int nodeID) {
        return new EthereumMinerNode(nodeID, sampleMinerRegionBitcoin(), sampleHashPowerEthereum());
    }

    public static EthereumCasperNode createNewEthereumCasperNode(int nodeID, int checkpointSpace, int numOfStakeholders) {
        return new EthereumCasperNode(nodeID, sampleRegionEthereum(), checkpointSpace, numOfStakeholders);
    }

    public static EthereumCasperMiner createNewEthereumCasperMiner(int nodeID, int checkpointSpace, int numOfStakeholders) {
        return new EthereumCasperMiner(nodeID, sampleMinerRegionBitcoin(), sampleHashPowerEthereum(), checkpointSpace, numOfStakeholders);
    }

    public static EthereumDAGsperNode createNewEthereumDAGsperNode(int nodeID, int checkpointSpace, int numOfStakeholders) {
        return new EthereumDAGsperNode(nodeID, sampleRegionEthereum(), checkpointSpace, numOfStakeholders);
    }

    public static EthereumDAGsperMiner createNewEthereumDAGsperMiner(int nodeID, int checkpointSpace, int numOfStakeholders) {
        return new EthereumDAGsperMiner(nodeID, sampleMinerRegionBitcoin(), sampleHashPowerEthereum(), checkpointSpace, numOfStakeholders);
    }

    public static BitcoinNode createNewBitcoinNode(int nodeID) {
        return new BitcoinNode(nodeID, sampleRegionBitcoin());
    }

    public static PBFTNode createNewPBFTNode(int nodeID, int numAllParticipants) {
        return new PBFTNode(nodeID, sampleRegionBitcoin(), numAllParticipants);
    }

    private static int sampleRegionEthereum() {
        return sampleFromDistribution(ETHEREUM_REGION_DISTRIBUTION_2020);
    }

    private static int sampleMinerRegionBitcoin() {
        return sampleFromDistribution(MINER_REGION_DISTRIBUTION_BITCOIN_2020);
    }

    private static int sampleRegionBitcoin() {
        return sampleFromDistribution(BITCOIN_REGION_DISTRIBUTION_2019);
    }

    private static long sampleHashPowerEthereum() {
        return sampleDistributionWithBins(ETHEREUM_HASH_POWER_DISTRIBUTION, ETHEREUM_HASH_POWER_DISTRIBUTION_BIN);
    }

}
