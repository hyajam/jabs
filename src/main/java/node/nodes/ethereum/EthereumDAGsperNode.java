package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.AbstractBlockchainConsensus;
import main.java.consensus.DAGsper;
import main.java.data.ethereum.EthereumBlock;
import main.java.data.ethereum.EthereumTx;

public class EthereumDAGsperNode extends EthereumNode {
    public EthereumDAGsperNode(int nodeID, int region, int checkpointSpace, int numOfStakeholders) {
        super(nodeID, region,
                new DAGsper<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
