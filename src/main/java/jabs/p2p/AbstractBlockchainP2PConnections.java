package jabs.p2p;

import jabs.network.Network;
import jabs.node.nodes.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author habib yajam
 */
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

    public void connectToNetwork(Network network){
        this.network = network;
        node.getNodeNetworkInterface().connectNetwork(network, network.getRandom());
        while (this.outbound.size() < this.numOutboundConnections) {
            Node remoteNode = network.getRandom().sampleFromList(network.getAllNodes());
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
