package main.java.event;

import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

import static main.java.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class PacketSendingProcess extends AbstractPacketProcessor {
    public PacketSendingProcess(Network network, Node node) {
        super(network, node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        PacketDeliveryEvent transfer = new PacketDeliveryEvent(packet);
        long latency = network.getLatency(packet.getFrom(), packet.getTo());
        Simulator.putEvent(transfer, latency);
    }

    public long processingTime(Packet packet) {
        return ((packet.getSize()*8) / (node.getNodeNetworkInterface().uploadBandwidth/1000)) + PACKET_PROCESSING_TIME;
    }
}
