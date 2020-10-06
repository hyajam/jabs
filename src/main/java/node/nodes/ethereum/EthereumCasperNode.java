package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.CasperFFG;
import main.java.network.Network;
import main.java.simulator.Simulator;

public class EthereumCasperNode extends EthereumNode {
    public EthereumCasperNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, int checkpointSpace, int numOfStakeholders) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
