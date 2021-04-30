package jabs.node.nodes.ethereum;

import jabs.blockchain.LocalBlockTree;
import jabs.consensus.CasperFFG;
import jabs.network.Network;
import jabs.simulator.Simulator;

public class EthereumCasperMiner extends EthereumMinerNode {
    public EthereumCasperMiner(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, int checkpointSpace, int numOfStakeholders) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower,
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), checkpointSpace, numOfStakeholders));
    }
}
