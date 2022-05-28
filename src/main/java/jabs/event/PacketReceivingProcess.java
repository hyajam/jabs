package jabs.event;

import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import static jabs.config.SimulationConfig.PACKET_PROCESSING_TIME;

public class PacketReceivingProcess extends AbstractPacketProcessor {
    public PacketReceivingProcess(Simulator simulator, Network network, RandomnessEngine randomnessEngine, Node node) {
        super(simulator, network, randomnessEngine, node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        this.node.processIncomingPacket(packet);
    }

    public double processingTime(Packet packet) {
        return ((packet.getSize()*8) / (((double) node.getNodeNetworkInterface().downloadBandwidth))) + PACKET_PROCESSING_TIME;
    }
}

