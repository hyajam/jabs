package main.java.p2p;

import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.random.Random;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBlockchainP2PConnections extends AbstractP2PConnections {
    protected final int numOutboundConnections;
    private final int maxConnections;

    private final List<Node> outbound = new ArrayList<>();
    private final List<Node> inbound = new ArrayList<>();

    protected Network network;


    public AbstractBlockchainP2PConnections(int numOutboundConnections, int maxConnections) {
        this.numOutboundConnections = numOutboundConnections;
        this.maxConnections = maxConnections;
    }

    public void connectToNetwork(Network network, Random random){
        this.network = network;
        node.getNodeNetworkInterface().connectNetwork(network, random);
        while (this.outbound.size() < this.numOutboundConnections) {
            Node remoteNode = random.sampleFromList(network.getAllNodes());
            if (remoteNode != this.getNode() &&
                    !this.outbound.contains(remoteNode) &&
                    !this.inbound.contains(remoteNode)) {
                if (remoteNode.getP2pConnections().requestConnection(this.getNode())) {
                    this.addOutbound(remoteNode);
                }
            }
        }
    }

    public boolean requestConnection(Node remoteNode) {
        if (this.inbound.size() <= (this.maxConnections - this.numOutboundConnections)) {
            this.inbound.add(remoteNode);
            this.neighbors.add(remoteNode);
            return true;
        } else {
            return false;
        }
    }

    public void addOutbound(Node remoteNode) {
        this.outbound.add(remoteNode);
        this.neighbors.add(remoteNode);
    }
}
