package main.java.event;

import main.java.simulator.Simulator;

import static main.java.random.Random.sampleExponentialDistribution;

public abstract class AbstractGeneratorProcess implements Event {
    private final long averageTimeBetweenGenerations;

    public AbstractGeneratorProcess(long averageTimeBetweenGenerations) {
        this.averageTimeBetweenGenerations = averageTimeBetweenGenerations;
    }

    @Override
    public void execute() {
        this.generateAndSetNextEvent();
    }

    protected void generateAndSetNextEvent() {
        this.generate();
        Simulator.putEvent(this, this.timeToNextGeneration());
    }

    protected long timeToNextGeneration() {
        return sampleExponentialDistribution(averageTimeBetweenGenerations);
    }

    protected abstract void generate();
}
