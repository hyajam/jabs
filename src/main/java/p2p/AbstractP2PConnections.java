package main.java.p2p;

import main.java.data.Data;
import main.java.node.nodes.Node;

import java.util.ArrayList;
import java.util.List;

// TODO recheck if it is a better method for implementing Abstract Routing Table
public abstract class AbstractP2PConnections {
    private Node node;
    protected final List<Node> neighbors = new ArrayList<>();

    protected Node getNode() {
        return node;
    }
    public void setNode(Node node) {
        this.node = node;
    }
    public List<Node> getNeighbors(){
        return this.neighbors;
    }
    public abstract void connectToNetwork();
    public abstract boolean requestConnection(Node node);
}
