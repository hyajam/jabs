package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.DAGsper;
import main.java.network.Network;
import main.java.simulator.Simulator;

public class EthereumDAGsperNode extends EthereumNode {
    public EthereumDAGsperNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, int checkpointSpace, int numOfStakeholders) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new DAGsper<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
