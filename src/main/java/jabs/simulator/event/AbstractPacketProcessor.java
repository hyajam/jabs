package jabs.simulator.event;

import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.PriorityQueue;

public abstract class AbstractPacketProcessor implements Event {
    protected final Simulator simulator;
    protected final Network network;
    protected final RandomnessEngine randomnessEngine;
    protected final PriorityQueue<TimedPacket> packetsQueue = new PriorityQueue<>();

    private record TimedPacket(Packet packet, double time) implements Comparable<TimedPacket> {
        public int compareTo(TimedPacket o) {
                return Double.compare(this.time, o.time);
            }
    }

    protected final Node node;

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
        TimedPacket timedPacket = new TimedPacket(packet, simulator.getSimulationTime());
        packetsQueue.add(timedPacket);
    }

    public Packet peek() {
        TimedPacket timedPacket = this.packetsQueue.peek();
        return timedPacket.packet;
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

    public abstract double processingTime(Packet packet);
    protected abstract void sendPacketToNextProcess(Packet packet);

    public Node getNode() {
        return node;
    }
}
