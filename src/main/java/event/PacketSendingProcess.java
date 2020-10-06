package main.java.event;

import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.random.Random;
import main.java.simulator.Simulator;

import static main.java.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class PacketSendingProcess extends AbstractPacketProcessor {
    public PacketSendingProcess(Simulator simulator, Network network, Random random, Node node) {
        super(simulator, network, random, node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        PacketDeliveryEvent transfer = new PacketDeliveryEvent(packet);
        long latency = network.getLatency(packet.getFrom(), packet.getTo());
        simulator.putEvent(transfer, latency);
    }

    public long processingTime(Packet packet) {
        return ((packet.getSize()*8) / (node.getNodeNetworkInterface().uploadBandwidth/1000)) + PACKET_PROCESSING_TIME;
    }
}
