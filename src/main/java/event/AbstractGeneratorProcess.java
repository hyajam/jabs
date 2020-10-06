package main.java.event;

import main.java.random.Random;
import main.java.simulator.Simulator;

public abstract class AbstractGeneratorProcess implements Event {
    private final long averageTimeBetweenGenerations;
    protected final Simulator simulator;

    public AbstractGeneratorProcess(Simulator simulator, long averageTimeBetweenGenerations) {
        this.simulator = simulator;
        this.averageTimeBetweenGenerations = averageTimeBetweenGenerations;
    }

    @Override
    public void execute() {
        this.generateAndSetNextEvent();
    }

    protected void generateAndSetNextEvent() {
        this.generate();
        simulator.putEvent(this, this.timeToNextGeneration());
    }

    protected long timeToNextGeneration(Random random) {
        return random.sampleExponentialDistribution(averageTimeBetweenGenerations);
    }

    protected abstract void generate();
}
