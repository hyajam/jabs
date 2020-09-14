package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.DAGsper;

public class EthereumDAGsperMiner extends EthereumMinerNode {
    public EthereumDAGsperMiner(int nodeID, int region, long hashPower, int checkpointSpace, int numOfStakeholders) {
        super(nodeID, region, hashPower,
                new DAGsper<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
