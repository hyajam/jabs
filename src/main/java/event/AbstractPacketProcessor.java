package main.java.event;

import main.java.message.Packet;
import main.java.network.Network;
import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

import java.util.PriorityQueue;

public abstract class AbstractPacketProcessor implements Event {
    protected final Network network;
    protected final PriorityQueue<TimedPacket> packetsQueue = new PriorityQueue<>();

    private static class TimedPacket implements Comparable<TimedPacket> {
        private final Packet packet;
        private final long time;

        private TimedPacket(Packet packet, long time){
            this.packet = packet;
            this.time = time;
        }

        public int compareTo(TimedPacket o) {
            return Long.compare(this.time, o.time);
        }
    }

    public final Node node;

    public AbstractPacketProcessor(Network network, Node node) {
        this.network = network;
        this.node = node;
    }

    public boolean isQueueEmpty() {
        return (packetsQueue.isEmpty());
    }

    public void addToQueue(Packet packet) {
        TimedPacket timedPacket = new TimedPacket(packet, Simulator.getCurrentTime());
        packetsQueue.add(timedPacket);
    }

    public void execute() {
        TimedPacket timedPacket = this.packetsQueue.poll();
        if (timedPacket != null) {
            if (!this.packetsQueue.isEmpty()) {
                Simulator.putEvent(this, processingTime(timedPacket.packet));
            }
            this.sendPacketToNextProcess(timedPacket.packet);
        }
    }

    public abstract long processingTime(Packet packet);
    protected abstract void sendPacketToNextProcess(Packet packet);
}
