package jabs.network.node.nodes.ethereum;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.algorithm.CasperFFG;
import jabs.consensus.config.CasperFFGConfig;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.networks.Network;
import jabs.simulator.Simulator;

public class EthereumCasperMiner extends EthereumMinerNode {
    public EthereumCasperMiner(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                               long uploadBandwidth, double hashPower, EthereumBlock genesisBlock,
                               CasperFFGConfig casperFFGConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower,
                new CasperFFG<>(new LocalBlockTree<>(genesisBlock), casperFFGConfig));
    }
}
