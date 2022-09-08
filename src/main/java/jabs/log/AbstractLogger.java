package jabs.log;

import jabs.scenario.AbstractScenario;
import jabs.simulator.event.Event;

import java.io.IOException;

public abstract class AbstractLogger {
    /**
     * The main scenario in which the logger is working
     */
    protected AbstractScenario scenario;

    /**
     * Sets the scenario of the logger. Since logger is created in constructor of the
     * scenario this should be set later.
     * @param scenario
     */
    public void setScenario(AbstractScenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Starting log before starting simulation. Can be used for writing general
     * information about the simulation parameters
     */
    abstract public void initialLog();

    /**
     * Before each event this function will be called. This can be used to log
     * information that emphasis on the changes each event causes.
     */
    abstract public void logBeforeEachEvent(Event event);

    /**
     * This function will be called after each event. Most information should
     * be logged using this function.
     */
    abstract public void logAfterEachEvent(Event event);

    /**
     * At the end of the scenario this function will be called and can be used for
     * logging info that concludes and show results.
     * @throws IOException
     */
    abstract public void finalLog() throws IOException;
}
