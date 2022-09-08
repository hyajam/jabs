package jabs.network.node.nodes;

import jabs.network.message.Message;
import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.NetworkInterface;
import jabs.network.p2p.AbstractP2PConnections;
import jabs.simulator.Simulator;

public abstract class Node {
    /**
     * Node's ID (integer number which identifies node in its network)
     */
    public final int nodeID;

    /**
     * Node's network interface
     * This object handles all packet sending/receiving in network
     */
    protected final NetworkInterface networkInterface;

    /**
     * Node's P2P connections
     * This handles neighbors of the node in the network
     */
    protected final AbstractP2PConnections p2pConnections;

    /**
     * Node's simulator
     * This handles events and their order and executes them
     */
    protected final Simulator simulator;

    /**
     * Node's network
     * This object have all nodes in it. Delays and latencies are configured here.
     */
    protected final Network network;

    /**
     * Creates a node and set its network, ID, upload and download bandwidth, and its P2P connections
     * @param simulator simulator which will simulate this node's actions
     * @param network Network in which the node operates
     * @param nodeID Node's ID in the network which is an integer number
     * @param downloadBandwidth Node's total download bandwidth
     * @param uploadBandwidth Node's total upload bandwidth
     * @param p2pConnections A P2P abstract class which handles neighbors and connections to them
     */
    public Node(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, AbstractP2PConnections p2pConnections) {
        this.nodeID = nodeID;
        this.network = network;
        this.networkInterface = new NetworkInterface(simulator,this, downloadBandwidth, uploadBandwidth);
        this.p2pConnections = p2pConnections;
        this.p2pConnections.setNode(this);
        this.simulator = simulator;
    }

    /**
     * This method is called when a new packet is received by the network interface.
     * This should run the protocol and execute the required reaction according to the protocol for the incoming data
     * For example this might process a new incoming transaction or block.
     *
     * @param packet incoming packet
     */
    public abstract void processIncomingPacket(Packet packet);

    /**
     * This method forces the node to generate a new simulated transaction
     */
    public abstract void generateNewTransaction();

    /**
     * Simulates a crash fault. It stops packet delivery to and from the node.
     */
    public void crash() {
        networkInterface.takeDown();
    }

    /**
     * Restores the node from a crash fault. New packets can be delivered to and from the node.
     */
    public void restore() {
        networkInterface.bringUp();
    }

    /**
     * Returns node network interface
     * @return nodes network interface
     */
    public NetworkInterface getNodeNetworkInterface() {
        return this.networkInterface;
    }

    /**
     * Returns node's P2P connection class
     * @return Node's P2P connection class
     */
    public AbstractP2PConnections getP2pConnections() {
        return this.p2pConnections;
    }

    /**
     * Forces the node to broadcast a message to all its neighbors
     * @param message The message to be broadcasted to all neighbors
     */
    public void broadcastMessage(Message message) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.networkInterface.addToUpLinkQueue(
                    new Packet(this, neighbor, message)
            );
        }
    }

    /**
     * Returns node's simulator
     * @return node's simulator
     */
    public Simulator getSimulator() {
        return this.simulator;
    }

    /**
     * Returns node's network
     * @return node's network
     */
    public Network getNetwork() {
        return this.network;
    }

    /**
     * getter method for node ID
     * @return node's ID
     */
    public int getNodeID() {
        return this.nodeID;
    }
}

