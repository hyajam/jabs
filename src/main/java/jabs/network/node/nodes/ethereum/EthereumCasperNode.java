package jabs.network.node.nodes.ethereum;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.algorithm.CasperFFG;
import jabs.consensus.config.CasperFFGConfig;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.networks.Network;
import jabs.simulator.Simulator;

public class EthereumCasperNode extends EthereumNode {
    public EthereumCasperNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                              long uploadBandwidth, EthereumBlock genesisBlock, CasperFFGConfig casperFFGConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new CasperFFG<>(new LocalBlockTree<>(genesisBlock), casperFFGConfig));
    }
}
