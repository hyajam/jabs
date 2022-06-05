package jabs.network.node.nodes.ethereum;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.algorithm.CasperFFG;
import jabs.network.networks.Network;
import jabs.simulator.Simulator;

public class EthereumCasperMiner extends EthereumMinerNode {
    public EthereumCasperMiner(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, int checkpointSpace, int numOfStakeholders) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower,
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
