package main.java.simulator;

import main.java.event.Event;

import java.util.PriorityQueue;

public abstract class AbstractSimulator {
    private static final PriorityQueue<ScheduledEvent> eventQueue = new PriorityQueue<>();

    private static class ScheduledEvent implements Comparable<ScheduledEvent> {
        private final Event event;
        private final long scheduledTime;

        private ScheduledEvent(Event event, long scheduledTime){
            this.event = event;
            this.scheduledTime = scheduledTime;
        }

        private Event getEvent(){ return this.event; }
        private long getScheduledTime(){ return this.scheduledTime; }

        public int compareTo(ScheduledEvent o) {
            return Long.compare(this.scheduledTime, o.scheduledTime);
        }
    }

    private static long currentTime = 0L;

    public static void executeNextEvent(){
        if (!eventQueue.isEmpty()) {
            ScheduledEvent currentScheduledEvent = eventQueue.poll();
            Event currentEvent = currentScheduledEvent.getEvent();
            currentTime = currentScheduledEvent.getScheduledTime();
            currentEvent.execute();
        }
    }

    public static Event peekEvent(){
        if (!eventQueue.isEmpty()) {
            ScheduledEvent currentEvent = eventQueue.peek();
            return currentEvent.getEvent();
        } else {
            return null;
        }
    }

    public static boolean thereIsMoreEvents() {
        return !eventQueue.isEmpty();
    }

    public static void putEvent(Event event, long remainingTimeToExecution){
        ScheduledEvent sEvent = new ScheduledEvent(event, currentTime + remainingTimeToExecution);
        eventQueue.add(sEvent);
    }

    public static void putEventWithAbsoluteTime(Event Event, long AbsoluteTime){
        ScheduledEvent sEvent = new ScheduledEvent(Event, AbsoluteTime);
        eventQueue.add(sEvent);
    }

    public static long getCurrentTime(){ return currentTime; }
}


