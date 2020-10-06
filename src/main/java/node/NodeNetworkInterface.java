package main.java.node;

import main.java.event.AbstractPacketProcessor;
import main.java.event.PacketReceivingProcess;
import main.java.event.PacketSendingProcess;
import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.random.Random;
import main.java.simulator.Simulator;

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
