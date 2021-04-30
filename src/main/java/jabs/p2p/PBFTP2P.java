package jabs.p2p;

import jabs.network.Network;
import jabs.node.nodes.Node;

public class PBFTP2P extends AbstractP2PConnections {
    @Override
    public void connectToNetwork(Network network) {
        this.neighbors.addAll(network.getAllNodes());
    }

    @Override
    public boolean requestConnection(Node node) {
        return false;
    }
}
