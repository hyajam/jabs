package main.java.event;

import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.message.Message;
import main.java.simulator.AbstractSimulator;

import static main.java.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class MessageSendingProcess extends AbstractMessageProcessor {
    public MessageSendingProcess(Node node) {
        super(node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        MessageDeliveryEvent transfer = new MessageDeliveryEvent(packet);
        long latency = Network.getLatency(packet.getFrom().region, packet.getTo().region);
        AbstractSimulator.putEvent(transfer, latency);
    }

    protected long processingTime(Packet packet) {
        return ((packet.getSize()*8) / (node.getNodeNetworkInterface().uploadBandwidth/1000)) + PACKET_PROCESSING_TIME;
    }
}
