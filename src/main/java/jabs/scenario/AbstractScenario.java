package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.networks.Network;
import jabs.simulator.event.Event;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    protected List<AbstractLogger> loggers = new ArrayList<>();
    long progressMessageIntervals;
    final String name;

    /**
     * Returns the network of the scenario. This can be used for accessing nodes inside the network.
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
     * @return simulator the name of this simulation.
     */
    public String getName() {
        return this.name;
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
     */
    public AbstractScenario(String name, long seed) {
        this.randomnessEngine = new RandomnessEngine(seed);
        this.name = name;
        simulator = new Simulator();
        this.progressMessageIntervals = TimeUnit.SECONDS.toNanos(2);
    }

    public void AddNewLogger(AbstractLogger logger) {
        this.loggers.add(logger);
    }

    /**
     * Sets the interval between two in progress messages
     * @param progressMessageIntervals the progress message interval described in nanoseconds
     */
    public void setProgressMessageIntervals(long progressMessageIntervals) {
        this.progressMessageIntervals = progressMessageIntervals;
    }

    /**
     * When called starts the simulation and runs everything to the end of simulation. This also
     * logs events using the logger object.
     */
    public void run() throws IOException {
        System.err.printf("Staring %s...\n", this.name);
        this.createNetwork();
        this.insertInitialEvents();

        for (AbstractLogger logger:this.loggers) {
            logger.setScenario(this);
            logger.initialLog();
        }
        long simulationStartingTime = System.nanoTime();
        long lastProgressMessageTime = simulationStartingTime;
        while (simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            Event event = simulator.peekEvent();
            for (AbstractLogger logger:this.loggers) {
                logger.logBeforeEvent(event);
            }
            simulator.executeNextEvent();
            for (AbstractLogger logger:this.loggers) {
                logger.logAfterEvent(event);
            }
            if (System.nanoTime() - lastProgressMessageTime > this.progressMessageIntervals) {
                double realTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - simulationStartingTime);
                double simulationTime = this.simulator.getCurrentTime();
                System.err.printf(
                        "Simulation in progress... " +
                                "Elapsed Real Time: %d:%02d:%02d, Elapsed Simulation Time: %d:%02d:%02d\n",
                        (long)(realTime / 3600), (long)((realTime % 3600) / 60), (long)(realTime % 60),
                        (long)(simulationTime / 3600), (long)((simulationTime % 3600) / 60), (long)(simulationTime % 60)
                );
                lastProgressMessageTime = System.nanoTime();
            }
        }
        for (AbstractLogger logger:this.loggers) {
            logger.finalLog();
        }

        System.err.printf("Finished %s.\n", this.name);
    }
}
