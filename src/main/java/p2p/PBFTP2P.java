package main.java.p2p;

import main.java.network.Network;
import main.java.node.nodes.Node;

public class PBFTP2P extends AbstractP2PConnections {
    @Override
    public void connectToNetwork() {
        this.neighbors.addAll(Network.getAllNodes());
    }

    @Override
    public boolean requestConnection(Node node) {
        return false;
    }
}
