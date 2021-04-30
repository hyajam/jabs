package jabs.node.nodes.bitcoin;

import jabs.network.Network;
import jabs.node.nodes.MinerNode;
import jabs.simulator.Simulator;

public class BitcoinMinerNode extends BitcoinNode implements MinerNode {
    public BitcoinMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth);
    }

    @Override
    public void generateNewBlock() {

    }

    @Override
    public long getHashPower() {
        return 0;
    }
}
