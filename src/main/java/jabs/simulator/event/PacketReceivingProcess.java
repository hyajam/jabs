package jabs.simulator.event;

import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class PacketReceivingProcess extends AbstractPacketProcessor {
    public PacketReceivingProcess(Simulator simulator, Network network, RandomnessEngine randomnessEngine, Node node) {
        super(simulator, network, randomnessEngine, node);
    }

    protected void sendPacketToNextProcess(Packet packet) {
        this.node.processIncomingPacket(packet);
    }

    public double processingTime(Packet packet) {
        return ((packet.getSize()*8) / (((double) node.getNodeNetworkInterface().downloadBandwidth)));
    }
}

