package jabs.simulator;

import jabs.event.Event;
import java.util.PriorityQueue;

public class Simulator {
    private final PriorityQueue<ScheduledEvent> eventQueue = new PriorityQueue<>();
    private long currentTime = 0L;
    private long insertedEvents = 0;

    private static class ScheduledEvent implements Comparable<ScheduledEvent> {
        private final Event event;
        private final long time;
        private final long number;

        private ScheduledEvent(Event event, long time, long number){
            this.event = event;
            this.time = time;
            this.number = number;
        }

        private Event getEvent(){ return this.event; }
        private long getTime(){ return this.time; }

        public int compareTo(ScheduledEvent o) {
            return (this.time < o.time) ? -1 : ((this.time > o.time) ? 1 : (Long.compare(this.number, o.number)));
        }
    }

    public void executeNextEvent(){
        if (!eventQueue.isEmpty()) {
            ScheduledEvent currentScheduledEvent = eventQueue.poll();
            Event currentEvent = currentScheduledEvent.getEvent();
            currentTime = currentScheduledEvent.getTime();
            currentEvent.execute();
        }
    }

    public Event peekEvent(){
        if (!eventQueue.isEmpty()) {
            ScheduledEvent currentEvent = eventQueue.peek();
            return currentEvent.getEvent();
        } else {
            return null;
        }
    }

    public boolean thereIsMoreEvents() {
        return !eventQueue.isEmpty();
    }

    public void putEvent(Event event, long remainingTimeToExecution){
        ScheduledEvent sEvent = new ScheduledEvent(event, currentTime + remainingTimeToExecution, insertedEvents);
        eventQueue.add(sEvent);
        insertedEvents++;
    }

    public long getCurrentTime() { return currentTime; }

    public void reset() {
        eventQueue.clear();
        currentTime = 0;
    }
}


