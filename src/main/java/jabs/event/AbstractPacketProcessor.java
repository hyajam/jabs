package jabs.event;

import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.PriorityQueue;

public abstract class AbstractPacketProcessor implements Event {
    protected final Simulator simulator;
    protected final Network network;
    protected final RandomnessEngine randomnessEngine;
    protected final PriorityQueue<TimedPacket> packetsQueue = new PriorityQueue<>();

    private static class TimedPacket implements Comparable<TimedPacket> {
        private final Packet packet;
        private final double time;

        private TimedPacket(Packet packet, double time){
            this.packet = packet;
            this.time = time;
        }

        public int compareTo(TimedPacket o) {
            return Double.compare(this.time, o.time);
        }
    }

    public final Node node;

    public AbstractPacketProcessor(Simulator simulator, Network network, RandomnessEngine randomnessEngine, Node node) {
        this.simulator = simulator;
        this.network = network;
        this.randomnessEngine = randomnessEngine;
        this.node = node;
    }

    public boolean isQueueEmpty() {
        return (packetsQueue.isEmpty());
    }

    public void addToQueue(Packet packet) {
        TimedPacket timedPacket = new TimedPacket(packet, simulator.getCurrentTime());
        packetsQueue.add(timedPacket);
    }

    public void execute() {
        TimedPacket timedPacket = this.packetsQueue.poll();
        if (timedPacket != null) {
            if (!this.packetsQueue.isEmpty()) {
                simulator.putEvent(this, processingTime(timedPacket.packet));
            }
            this.sendPacketToNextProcess(timedPacket.packet);
        }
    }

    public abstract long processingTime(Packet packet);
    protected abstract void sendPacketToNextProcess(Packet packet);
}
