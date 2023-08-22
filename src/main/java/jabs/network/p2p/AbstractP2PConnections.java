package jabs.network.p2p;

import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;

import java.util.ArrayList;
import java.util.List;

// TODO recheck if it is a better method for implementing Abstract Routing Table
public abstract class AbstractP2PConnections {
    protected Node node;
    protected final List<Node> peerNeighbors = new ArrayList<>();

    protected Node getNode() {
        return node;
    }
    public void setNode(Node node) {
        this.node = node;
    }
    public List<Node> getNeighbors(){
        return this.peerNeighbors;
    }
    public abstract void connectToNetwork(Network network);
    public abstract boolean requestConnection(Node node);
}
