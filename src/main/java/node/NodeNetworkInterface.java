package main.java.node;

import main.java.event.AbstractMessageProcessor;
import main.java.event.MessageReceivingProcess;
import main.java.event.MessageSendingProcess;
import main.java.message.Packet;
import main.java.node.nodes.Node;
import main.java.simulator.AbstractSimulator;

public class NodeNetworkInterface {
    public final long downloadBandwidth;
    public final long uploadBandwidth;

    public final MessageReceivingProcess messageReceivingProcess;
    public final MessageSendingProcess messageSendingProcess;

    public NodeNetworkInterface(Node node, long downloadBandwidth, long uploadBandwidth) {
        this.downloadBandwidth = downloadBandwidth;
        this.uploadBandwidth = uploadBandwidth;
        this.messageReceivingProcess = new MessageReceivingProcess(node);
        this.messageSendingProcess = new MessageSendingProcess(node);
    }

    public void addToDownLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageReceivingProcess); }
    public void addToUpLinkQueue(Packet packet) { this.addToLinkQueue(packet, this.messageSendingProcess); }
    private void addToLinkQueue(Packet packet, AbstractMessageProcessor Processor) {
        if (Processor.isQueueEmpty()) {
            AbstractSimulator.putEvent(Processor, 0);
        }
        Processor.addToQueue(packet);
    }
}
