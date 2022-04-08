package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.Network;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * An abstract class for defining a scenario.
 *
 */
public abstract class AbstractScenario {
    /**
     * network which is being used for simulation
     */
    protected Network network;
    protected Simulator simulator;
    protected RandomnessEngine randomnessEngine;
    protected AbstractLogger logger;
    long progressMessageIntervals;
    final String name;

    /**
     * Returns the network of the scenario. This is being used for accessing nodes inside the network.
     * @return network of this scenario
     */
    public Network getNetwork() {
        return this.network;
    }

    /**
     * Returns the simulator object that the scenario is using. This can be used to access the events in simulator.
     * @return simulator object of the scenario
     */
    public Simulator getSimulator() {
        return this.simulator;
    }

    /**
     * Create the network and set up the simulation environment.
     */
    abstract protected void createNetwork();

    /**
     * Insert initial events into the event queue.
     */
    abstract protected void insertInitialEvents();

    /**
     * runs before each event and checks if simulation should stop.
     * @return true if simulation should not continue to execution of next event.
     */
    abstract protected boolean simulationStopCondition();

    /**
     * creates an abstract scenario with a user defined name
     * @param seed this value gives the simulation a randomnessEngine seed
     * @param logger this is output log of the scenario
     */
    public AbstractScenario(String name, long seed, AbstractLogger logger) {
        this.randomnessEngine = new RandomnessEngine(seed);
        this.name = name;
        simulator = new Simulator();
        this.logger = logger;
        this.progressMessageIntervals = TimeUnit.SECONDS.toNanos(2);
    }

    /**
     * Sets the interval between two in progress messages
     * @param progressMessageIntervals the progress message interval described in nanoseconds
     */
    public void setProgressMessageIntervals(long progressMessageIntervals) {
        this.progressMessageIntervals = progressMessageIntervals;
    }

    /**
     * When called starts the simulation and runs everything to the end of simulation
     */
    public void run() throws IOException {
        System.err.printf("Staring %s...\n", this.getClass().getSimpleName());
        this.createNetwork();
        this.insertInitialEvents();

        logger.setScenario(this);
        logger.initialLog();
        long simulationStartingTime = System.nanoTime();
        long lastProgressMessageTime = simulationStartingTime;
        while (simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            logger.logBeforeEvent();
            simulator.executeNextEvent();
            logger.logAfterEvent();
            if (System.nanoTime() - lastProgressMessageTime > this.progressMessageIntervals) {
                System.err.printf("Simulation in progress. Elapsed Real Time: %d, Elapsed Simulation Time: %.0f\n",
                        TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - simulationStartingTime),
                        this.simulator.getCurrentTime() / 1000);
                lastProgressMessageTime = System.nanoTime();
            }
        }
        logger.finalLog();

        System.err.printf("Finished %s.\n", this.getClass().getSimpleName());
    }
}
