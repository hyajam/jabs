package jabs.node.nodes;

import jabs.message.Message;
import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.NodeNetworkInterface;
import jabs.p2p.AbstractP2PConnections;
import jabs.simulator.Simulator;

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

