package main.java.event;

import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.message.Message;
import main.java.simulator.AbstractSimulator;

import static main.java.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class MessageSendingProcess extends AbstractMessageProcessor {
    public MessageSendingProcess(Node node) {
        super(node);
    }

    protected void sendPacketToNextProcess(Message message) {
        MessageDeliveryEvent transfer = new MessageDeliveryEvent(message);
        long latency = Network.getLatency(message.getFrom().region, message.getTo().region);
        AbstractSimulator.putEvent(transfer, latency);
    }

    protected long processingTime(Message message) {
        return ((message.getSize()*8) / (node.getNodeNetworkInterface().uploadBandwidth/1000)) + PACKET_PROCESSING_TIME;
    }
}
