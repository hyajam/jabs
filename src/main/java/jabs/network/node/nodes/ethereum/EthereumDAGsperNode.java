package jabs.network.node.nodes.ethereum;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.algorithm.DAGsper;
import jabs.consensus.config.DAGsperConfig;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.networks.Network;
import jabs.simulator.Simulator;

public class EthereumDAGsperNode extends EthereumNode {
    public EthereumDAGsperNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                               long uploadBandwidth, EthereumBlock genesisBlock, DAGsperConfig daGsperConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new DAGsper<>(new LocalBlockTree<>(genesisBlock), daGsperConfig));
    }
}
