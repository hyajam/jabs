package main.java.scenario;

import static main.java.simulator.Simulator.executeNextEvent;

public abstract class AbstractScenario {
    abstract protected void setupSimulation(); // create the network and put starting events
    abstract protected boolean simulationStopCondition(); // runs before each event and checks if simulation should stop
    abstract protected void outputResults(); // when simulation is ended could be used to output results

    public void run() {
        this.setupSimulation();

        while (this.simulationStopCondition()) {
            executeNextEvent();
        }

        this.outputResults();
    }
}
