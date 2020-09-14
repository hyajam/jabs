package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.CasperFFG;

public class EthereumCasperNode extends EthereumNode {
    public EthereumCasperNode(int nodeID, int region, int checkpointSpace, int numOfStakeholders) {
        super(nodeID, region,
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
