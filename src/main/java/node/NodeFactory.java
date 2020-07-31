package main.java.node;

import main.java.node.nodes.bitcoin.BitcoinNode;
import main.java.node.nodes.ethereum.EthereumMinerNode;
import main.java.node.nodes.ethereum.EthereumNode;

import static main.java.config.NetworkStats.*;
import static main.java.random.RandomSampling.sampleDistributionWithBins;
import static main.java.random.RandomSampling.sampleFromDistribution;

public final class NodeFactory {
    public static EthereumNode createNewEthereumNode(int nodeID) {
        return new EthereumNode(nodeID, sampleRegionEthereum());
    }

    public static EthereumMinerNode createNewEthereumMiner(int nodeID) {
        return new EthereumMinerNode(nodeID, sampleMinerRegionBitcoin(), sampleHashPowerEthereum());
    }

    public static BitcoinNode createNewBitcoinNode(int nodeID) {
        return new BitcoinNode(nodeID, sampleRegionBitcoin());
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

//    private static long sampleNumOfConnectionsEthereum() {
//        return sampleDistributionWithBins(ETHEREUM_DEGREE_DISTRIBUTION_2020, ETHEREUM_DEGREE_DISTRIBUTION_2020_BIN);
//    }
//
//    private static long sampleNumOfConnectionsBitcoin() {
//        return sampleDistributionWithBins(BITCOIN_DEGREE_DISTRIBUTION_2015, BITCOIN_DEGREE_DISTRIBUTION_2015_BIN);
//    }
}
