package main.java.scenario;

import main.java.network.GlobalNetwork;
import main.java.network.Network;
import main.java.simulator.Simulator;
import static main.java.simulator.Simulator.executeNextEvent;

public abstract class AbstractScenario {
    Network network = new GlobalNetwork();

    abstract protected void setupSimulation(); // create the network and put starting events
    abstract protected boolean simulationStopCondition(); // runs before each event and checks if simulation should stop
    abstract protected void outputResults(); // when simulation is ended could be used to output results

    public void run() {
        this.setupSimulation();

        while (Simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            executeNextEvent();
        }

        this.outputResults();
    }
}
