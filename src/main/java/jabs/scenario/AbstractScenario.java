package jabs.scenario;

import jabs.network.Network;
import jabs.random.Random;
import jabs.simulator.Simulator;

public abstract class AbstractScenario {
    protected Network network;
    public Simulator simulator;
    protected Random random;

    abstract protected void createNetwork(); // create the network and setup the simulation environment
    abstract protected void insertInitialEvents(); // insert initial events into the event queue
    abstract protected boolean simulationStopCondition(); // runs before each event and checks if simulation should stop
    abstract protected void finishSimulation(); // when simulation is ended could be used to output results

    public void run() {
        simulator = new Simulator();
        this.createNetwork();
        this.insertInitialEvents();

        while (simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            simulator.executeNextEvent();
        }

        this.finishSimulation();
    }
}
