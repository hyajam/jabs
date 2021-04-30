package jabs.node.nodes.ethereum;

import jabs.blockchain.LocalBlockTree;
import jabs.consensus.DAGsper;
import jabs.network.Network;
import jabs.simulator.Simulator;

public class EthereumDAGsperMiner extends EthereumMinerNode {
    public EthereumDAGsperMiner(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, int checkpointSpace, int numOfStakeholders) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower,
                new DAGsper<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
