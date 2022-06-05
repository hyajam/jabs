package jabs.simulator.event;

import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class PacketSendingProcess extends AbstractPacketProcessor {
    public PacketSendingProcess(Simulator simulator, Network network, RandomnessEngine randomnessEngine, Node node) {
        super(simulator, network, randomnessEngine, node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        PacketDeliveryEvent transfer = new PacketDeliveryEvent(packet);
        double latency = network.getLatency(packet.getFrom(), packet.getTo());
        simulator.putEvent(transfer, latency);
    }

    public double processingTime(Packet packet) {
        return ((packet.getSize()*8) / ((double) node.getNodeNetworkInterface().uploadBandwidth));
    }
}
