package main.java.event;

import main.java.node.nodes.Node;
import main.java.message.Message;
import main.java.simulator.AbstractSimulator;

import java.util.PriorityQueue;

public abstract class AbstractMessageProcessor implements Event {
    protected final PriorityQueue<TimedMessage> packetsQueue = new PriorityQueue<>();

    private static class TimedMessage implements Comparable<TimedMessage> {
        private final Message message;
        private final long time;

        private TimedMessage(Message message, long time){
            this.message = message;
            this.time = time;
        }

        public int compareTo(TimedMessage o) {
            return Long.compare(this.time, o.time);
        }
    }

    public final Node node;

    public AbstractMessageProcessor(Node node) {
        this.node = node;
    }

    public boolean isQueueEmpty() {
        return (packetsQueue.isEmpty());
    }

    public void addToQueue(Message message) {
        TimedMessage timedMessage = new TimedMessage(message, AbstractSimulator.getCurrentTime());
        packetsQueue.add(timedMessage);
    }

    public void execute() {
        TimedMessage timedMessage = this.packetsQueue.poll();
        if (timedMessage != null) {
            if (!this.packetsQueue.isEmpty()) {
                AbstractSimulator.putEvent(this, processingTime(timedMessage.message));
            }
            this.sendPacketToNextProcess(timedMessage.message);
        }
    }

    abstract long processingTime(Message message);
    abstract void sendPacketToNextProcess(Message message);
}
