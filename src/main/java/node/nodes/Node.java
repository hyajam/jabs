package main.java.node.nodes;

import main.java.message.Message;
import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.NodeNetworkInterface;
import main.java.p2p.AbstractP2PConnections;
import main.java.simulator.Simulator;

public abstract class Node {
    public final int nodeID;
    protected final NodeNetworkInterface nodeNetworkInterface;
    protected final AbstractP2PConnections p2pConnections;
    protected final Simulator simulator;
    protected final Network network;

    public Node(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, AbstractP2PConnections p2pConnections) {
        this.nodeID = nodeID;
        this.network = network;
        this.nodeNetworkInterface = new NodeNetworkInterface(simulator,this, downloadBandwidth, uploadBandwidth);
        this.p2pConnections = p2pConnections;
        this.p2pConnections.setNode(this);
        this.simulator = simulator;
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

    public Simulator getSimulator() {
        return this.simulator;
    }

    public Network getNetwork() {
        return this.network;
    }
}

