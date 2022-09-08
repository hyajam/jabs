package jabs.simulator;

import jabs.simulator.event.Event;
import java.util.PriorityQueue;



/**
 * @author habib
 * A discrete time event-based simulator with event queue
 */
public class Simulator {

    /**
     * The queue that contains all events which are going to be executed. This
     * queue is a priority queue sorted by the time in which the event should
     * be executed.
     */
    private final PriorityQueue<ScheduledEvent> eventQueue = new PriorityQueue<>();

    /**
     * The simulation execution time of the most recent event
     */
    private double simulationTime = 0L;

    /**
     * Number of events inserted in the event queue till now (whether simulated
     * or not)
     */
    private long insertedEvents = 0;

    /**
     * @param event  The event
     * @param time   Simulation execution time of the event
     * @param number Event ID (insertion number in event queue)
     */
    public record ScheduledEvent(Event event, double time, long number) implements Comparable<ScheduledEvent> {
        /**
         * Returns the corresponding event
         *
         * @return the corresponding event
         */
        @Override
        public Event event() {
            return this.event;
        }

        /**
         * Returns the execution time of the event.
         *
         * @return the execution time of the event
         */
        @Override
        public double time() {
            return this.time;
        }

        /**
         * This function is used by the priority queue to sort the scheduled events
         *
         * @param o The scheduled event
         * @return -1 if the execution time of the object is earlier than the provided
         * input.
         * 0 if the execution time of the object is exactly equal to the
         * execution time of the provided input event.
         * 1 if the execution time of the object is after the execution of
         * the provided input.
         */
        public int compareTo(ScheduledEvent o) {
            return (this.time < o.time) ? -1 : ((this.time > o.time) ? 1 : (Long.compare(this.number, o.number)));
        }
    }

    /**
     * Executes the next event in the event queue
     */
    public void executeNextEvent() {
        if (!eventQueue.isEmpty()) {
            ScheduledEvent currentScheduledEvent = eventQueue.poll();
            Event currentEvent = currentScheduledEvent.event();
            simulationTime = currentScheduledEvent.time();
            currentEvent.execute();
        }
    }

    /**
     * Returns what is the next event to be executed without executing
     * the event.
     *
     * @return The next event to be executed in the simulator
     */
    public Event peekEvent() {
        if (!eventQueue.isEmpty()) {
            ScheduledEvent currentEvent = eventQueue.peek();
            return currentEvent.event();
        } else {
            return null;
        }
    }

    /**
     * Check if more events exist in the event queue to be simulated
     * @return True if there is any event in the queue
     */
    public boolean isThereMoreEvents() {
        return !eventQueue.isEmpty();
    }

    /**
     * Inserts a new event in event queue. The event execution time will be the
     * summation of current time and remaining time to execution.
     *
     * @param event The event to be executed
     * @param remainingTimeToExecution The time remaining to execution time of
     *                                 the event.
     * @return the scheduled event
     */
    public ScheduledEvent putEvent(Event event, double remainingTimeToExecution) {
        ScheduledEvent sEvent = new ScheduledEvent(event, simulationTime + remainingTimeToExecution, insertedEvents);
        eventQueue.add(sEvent);
        insertedEvents++;
        return sEvent;
    }

    /**
     * Removes an event already available in the event queue. It is specially useful
     * for processes that are ongoing such as packet receiving process or block mining
     * process.
     *
     * @param scheduledEvent The event to be executed
     */
    public void removeEvent(ScheduledEvent scheduledEvent) {
        this.eventQueue.remove(scheduledEvent);
    }

    /**
     * Returns the simulation time that the latest event has executed
     * @return Simulation time of the latest simulated event
     */
    public double getSimulationTime() { return simulationTime; }

    /**
     * Clears the event queue from any more events. Restarts the current time of
     * simulation to zero.
     */
    public void reset() {
        eventQueue.clear();
        simulationTime = 0;
    }
}


