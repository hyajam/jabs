package jabs.network.p2p;

import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;

public class SnowP2P extends AbstractP2PConnections{
    public void connectToNetwork(Network network) {
        this.peerNeighbors.addAll(network.getAllNodes());
        node.getNodeNetworkInterface().connectNetwork(network, network.getRandom());
    }

    @Override
    public boolean requestConnection(Node node) {
        return false;
    }
}
