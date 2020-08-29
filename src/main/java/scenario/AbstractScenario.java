package main.java.scenario;

public abstract class AbstractScenario {
    abstract public void simulationSetup(); // create the network and put starting events
    abstract public boolean simulationStopCondition(); // check whether simulation should stop
    abstract public void postSimulation(); // when simulation is ended could be used to output results
}
