package jabs.node;

import jabs.event.AbstractPacketProcessor;
import jabs.event.PacketReceivingProcess;
import jabs.event.PacketSendingProcess;
import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.nodes.Node;
import jabs.random.Random;
import jabs.simulator.Simulator;

public class NodeNetworkInterface {
    public final long downloadBandwidth;
    public final long uploadBandwidth;
    public final Node node;
    public final Simulator simulator;

    public PacketReceivingProcess messageReceivingProcess;
    public PacketSendingProcess messageSendingProcess;

    public NodeNetworkInterface(Simulator simulator, Node node, long downloadBandwidth, long uploadBandwidth) {
        this.simulator = simulator;
        this.downloadBandwidth = downloadBandwidth;
        this.uploadBandwidth = uploadBandwidth;
        this.node = node;
    }

    public void connectNetwork(Network network, Random random) {
        this.messageReceivingProcess = new PacketReceivingProcess(simulator, network, random, node);
        this.messageSendingProcess = new PacketSendingProcess(simulator, network, random, node);
    }

    public void addToDownLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageReceivingProcess); }
    public void addToUpLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageSendingProcess); }
    private void addToLinkQueue(Packet packet, AbstractPacketProcessor processor) {
        if (processor.isQueueEmpty()) {
            simulator.putEvent(processor, processor.processingTime(packet));
        }
        processor.addToQueue(packet);
    }
}
