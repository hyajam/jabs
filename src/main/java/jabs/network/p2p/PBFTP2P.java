package jabs.network.p2p;

import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;

public class PBFTP2P extends AbstractP2PConnections {
    @Override
    public void connectToNetwork(Network network) {
        this.peerNeighbors.addAll(network.getAllNodes());
        node.getNodeNetworkInterface().connectNetwork(network, network.getRandom());
    }

    @Override
    public boolean requestConnection(Node node) {
        return false;
    }
}
