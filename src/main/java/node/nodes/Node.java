package main.java.node.nodes;

import main.java.message.Message;
import main.java.message.Packet;
import main.java.node.NodeNetworkInterface;
import main.java.p2p.AbstractP2PConnections;

public abstract class Node {
    public final int nodeID;
    protected final NodeNetworkInterface nodeNetworkInterface;
    protected final AbstractP2PConnections p2pConnections;

    public Node(int nodeID, long downloadBandwidth, long uploadBandwidth, AbstractP2PConnections p2pConnections) {
        this.nodeID = nodeID;
        this.nodeNetworkInterface = new NodeNetworkInterface(this, downloadBandwidth, uploadBandwidth);
        this.p2pConnections = p2pConnections;
        this.p2pConnections.setNode(this);
    }

    public abstract void processIncomingPacket(Packet packet);
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

