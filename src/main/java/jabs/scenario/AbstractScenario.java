package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.Network;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.io.IOException;

public abstract class AbstractScenario {
    protected Network network;
    protected Simulator simulator;
    protected RandomnessEngine randomnessEngine;
    protected AbstractLogger logger;

    public Network getNetwork() {
        return this.network;
    }

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
     * creates an abstract scenario
     * @param seed this value gives the simulation a randomnessEngine seed
     * @param logger this is output log of the scenario
     */
    public AbstractScenario(long seed, AbstractLogger logger) {
        this.randomnessEngine = new RandomnessEngine(seed);
        simulator = new Simulator();
        this.logger = logger;
    }

    // TODO: add timed output that shows simulation is running...

    /**
     * When called starts the simulation and runs everything to the end of simulation
     */
    public void run() throws IOException {
        System.err.printf("Staring %s...\n", this.getClass().getSimpleName());
        this.createNetwork();
        this.insertInitialEvents();

        logger.setScenario(this);
        logger.initialLog();
        while (simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            logger.logBeforeEvent();
            simulator.executeNextEvent();
            logger.logAfterEvent();
        }
        logger.finalLog();

        System.err.printf("Finished %s.\n", this.getClass().getSimpleName());
    }
}
