package jabs.network.node.nodes.ethereum;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.algorithm.CasperFFG;
import jabs.network.networks.Network;
import jabs.simulator.Simulator;

public class EthereumCasperNode extends EthereumNode {
    public EthereumCasperNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, int checkpointSpace, int numOfStakeholders) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
