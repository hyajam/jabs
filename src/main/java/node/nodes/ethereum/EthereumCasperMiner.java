package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.CasperFFG;

public class EthereumCasperMiner extends EthereumMinerNode {
    public EthereumCasperMiner(int nodeID, int region, long hashPower, int checkpointSpace, int numOfStakeholders) {
        super(nodeID, region, hashPower,
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
