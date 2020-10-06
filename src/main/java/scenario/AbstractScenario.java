package main.java.scenario;

import main.java.network.Network;
import main.java.simulator.Simulator;

public abstract class AbstractScenario {
    protected Network network;
    protected Simulator simulator;

    abstract protected void setupSimulation(); // create the network and put starting events
    abstract protected boolean simulationStopCondition(); // runs before each event and checks if simulation should stop
    abstract protected void outputResults(); // when simulation is ended could be used to output results

    public void run() {
        simulator = new Simulator();
        this.setupSimulation();

        while (simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            simulator.executeNextEvent();
        }

        this.outputResults();
    }
}
