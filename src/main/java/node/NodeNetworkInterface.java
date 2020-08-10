package main.java.node;

import main.java.event.AbstractPacketProcessor;
import main.java.event.PacketReceivingProcess;
import main.java.event.PacketSendingProcess;
import main.java.message.Packet;
import main.java.node.nodes.Node;
import main.java.simulator.AbstractSimulator;

public class NodeNetworkInterface {
    public final long downloadBandwidth;
    public final long uploadBandwidth;

    public final PacketReceivingProcess messageReceivingProcess;
    public final PacketSendingProcess messageSendingProcess;

    public NodeNetworkInterface(Node node, long downloadBandwidth, long uploadBandwidth) {
        this.downloadBandwidth = downloadBandwidth;
        this.uploadBandwidth = uploadBandwidth;
        this.messageReceivingProcess = new PacketReceivingProcess(node);
        this.messageSendingProcess = new PacketSendingProcess(node);
    }

    public void addToDownLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageReceivingProcess); }
    public void addToUpLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageSendingProcess); }
    private void addToLinkQueue(Packet packet, AbstractPacketProcessor processor) {
        if (processor.isQueueEmpty()) {
            AbstractSimulator.putEvent(processor, processor.processingTime(packet));
        }
        processor.addToQueue(packet);
    }
}
