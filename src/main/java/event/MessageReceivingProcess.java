package main.java.event;

import main.java.message.Packet;
import main.java.node.nodes.Node;
import main.java.message.Message;
import static main.java.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class MessageReceivingProcess extends AbstractMessageProcessor {
    public MessageReceivingProcess(Node node) {
        super(node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        this.node.processIncomingMessage(packet);
    }

    protected long processingTime(Packet packet) {
        return ((packet.getSize()*8) / (node.getNodeNetworkInterface().downloadBandwidth/1000)) + PACKET_PROCESSING_TIME;
    }
}

