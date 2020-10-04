package main.java.node;

import main.java.event.AbstractPacketProcessor;
import main.java.event.PacketReceivingProcess;
import main.java.event.PacketSendingProcess;
import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

public class NodeNetworkInterface {
    public final long downloadBandwidth;
    public final long uploadBandwidth;
    public final Node node;

    public PacketReceivingProcess messageReceivingProcess;
    public PacketSendingProcess messageSendingProcess;

    public NodeNetworkInterface(Node node, long downloadBandwidth, long uploadBandwidth) {
        this.downloadBandwidth = downloadBandwidth;
        this.uploadBandwidth = uploadBandwidth;
        this.node = node;
    }

    public void connectNetwork(Network network) {
        this.messageReceivingProcess = new PacketReceivingProcess(network, node);
        this.messageSendingProcess = new PacketSendingProcess(network, node);
    }

    public void addToDownLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageReceivingProcess); }
    public void addToUpLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageSendingProcess); }
    private void addToLinkQueue(Packet packet, AbstractPacketProcessor processor) {
        if (processor.isQueueEmpty()) {
            Simulator.putEvent(processor, processor.processingTime(packet));
        }
        processor.addToQueue(packet);
    }
}
