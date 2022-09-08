package jabs.network.node;

import jabs.simulator.event.AbstractPacketProcessor;
import jabs.simulator.event.PacketReceivingProcess;
import jabs.simulator.event.PacketSendingProcess;
import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class NetworkInterface {
    /**
     * Node's total download bandwidth
     */
    public final long downloadBandwidth;

    /**
     * Node's total upload bandwidth
     */
    public final long uploadBandwidth;

    /**
     * The node that have this network interface
     */
    public final Node node;

    /**
     * The simulator which handles events for this network interface
     */
    public final Simulator simulator;

    /**
     * The process (which is a type of ongoing event) that handles receiving packets and their delays due to bandwidth
     */
    public PacketReceivingProcess messageReceivingProcess;

    /**
     * The process (which is a type of ongoing event) that handles sending packets and their delays due to bandwidth
     */
    public PacketSendingProcess messageSendingProcess;

    /**
     * State of network interface (false->Up / true->Down)
     */
    public boolean networkInterfaceDown;

    /**
     * Stops the node from sending or receiving packets
     */
    public void takeDown() {
        this.networkInterfaceDown = true;
    }

    /**
     * Restores the node network interface to a working state (node can send and receive packets again)
     */
    public void bringUp() {
        this.networkInterfaceDown = false;
    }

    /**
     * return the state of network interface
     * @return true if the network interface is down and not working
     */
    public boolean isNetworkInterfaceDown() {
        return networkInterfaceDown;
    }

    /**
     * Creates a new network interface
     * @param simulator The simulator which simulates the events of this network interface
     * @param node The node that possesses this network interface
     * @param downloadBandwidth The total down-link bandwidth of this node
     * @param uploadBandwidth The total up-link bandwidth of this node
     */
    public NetworkInterface(Simulator simulator, Node node, long downloadBandwidth, long uploadBandwidth) {
        this.simulator = simulator;
        this.downloadBandwidth = downloadBandwidth;
        this.uploadBandwidth = uploadBandwidth;
        this.node = node;
        this.networkInterfaceDown = false;
    }

    /**
     * Creates two processes (ongoing events) that handle receiving and sending packets and their respective latencies
     * @param network Network in which this network interface operates
     * @param randomnessEngine Randomness source for simulating randomness in delays
     */
    public void connectNetwork(Network network, RandomnessEngine randomnessEngine) {
        this.messageReceivingProcess = new PacketReceivingProcess(simulator, network, randomnessEngine, node);
        this.messageSendingProcess = new PacketSendingProcess(simulator, network, randomnessEngine, node);
    }

    /**
     * Adds the packet to node's receiving process if the network interface is not down
     * @param packet the packet to be received
     */
    public void addToDownLinkQueue(Packet packet) {
        if (!this.networkInterfaceDown) {
            this.addToLinkQueue(packet, this.messageReceivingProcess);
        }
    }

    /**
     * Adds the packet to node's sending process if the network interface is not down
     * @param packet the packet to be sent
     */
    public void addToUpLinkQueue(Packet packet) {
        if (!this.networkInterfaceDown) {
            this.addToLinkQueue(packet, this.messageSendingProcess);
        }
    }

    /**
     * Adds the packet to the queue of that link (up-link / down-link)
     * @param packet the packet
     * @param processor the up-link/down-link process
     */
    private void addToLinkQueue(Packet packet, AbstractPacketProcessor processor) {
        if (processor.isQueueEmpty()) {
            simulator.putEvent(processor, processor.processingTime(packet));
        }
        processor.addToQueue(packet);
    }
}
