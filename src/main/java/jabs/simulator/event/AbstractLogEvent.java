package jabs.simulator.event;

/**
 * The class of events extending AbstractLogEvent serve as a notification for logger to log an event. Sometimes things
 * happen inside nodes without having an event in the simulator queue. For example when a block is finalized there is no
 * apparent direct event in the simulator's event queue, but it might be needed to record that.
 */
public class AbstractLogEvent implements Event {
    protected final double time;

    public AbstractLogEvent(double time) {
        this.time = time;
    }

    /**
     * Log event does not have any execute function it acts as a medium to inform logger if it needs to record anything
     * new to the output.
     */
    @Override
    public void execute() {
    }
}
