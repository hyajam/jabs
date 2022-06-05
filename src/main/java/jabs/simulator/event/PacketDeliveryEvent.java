package jabs.simulator.event;

import jabs.network.message.Packet;

public class PacketDeliveryEvent implements Event {
    public final Packet packet;

    public PacketDeliveryEvent(Packet packet) {
        this.packet = packet;
    }

    public void execute(){
        this.packet.getTo().getNodeNetworkInterface().addToDownLinkQueue(this.packet);
    }
}
