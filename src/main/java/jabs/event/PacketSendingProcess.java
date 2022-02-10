package jabs.event;

import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import static jabs.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class PacketSendingProcess extends AbstractPacketProcessor {
    public PacketSendingProcess(Simulator simulator, Network network, RandomnessEngine randomnessEngine, Node node) {
        super(simulator, network, randomnessEngine, node);
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
