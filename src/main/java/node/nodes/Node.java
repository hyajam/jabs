package main.java.node.nodes;

import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.NodeNetworkInterface;
import main.java.p2p.AbstractP2PConnections;
import main.java.message.Message;

public abstract class Node {
    public final int nodeID;
    public final int region;
    protected final NodeNetworkInterface nodeNetworkInterface;
    protected final AbstractP2PConnections p2pConnections;

    public Node(int nodeID, int region, AbstractP2PConnections p2pConnections) {
        this.nodeID = nodeID;
        this.region = region;
        this.p2pConnections = p2pConnections;
        this.p2pConnections.setNode(this);
        this.nodeNetworkInterface = new NodeNetworkInterface(this,
                Network.sampleDownloadBandwidth(region), Network.sampleUploadBandwidth(region));
    }

    public abstract void processIncomingMessage(Packet packet);
    public abstract void generateNewTransaction();

    public NodeNetworkInterface getNodeNetworkInterface() {
        return this.nodeNetworkInterface;
    }

    public AbstractP2PConnections getP2pConnections() {
        return this.p2pConnections;
    }

    public void broadcastMessage(Message message) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.nodeNetworkInterface.addToUpLinkQueue(
                    new Packet(this, neighbor, message)
            );
        }
    }
}

