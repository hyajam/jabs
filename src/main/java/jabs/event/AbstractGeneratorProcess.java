package jabs.event;

import jabs.random.Random;
import jabs.simulator.Simulator;

public abstract class AbstractGeneratorProcess implements Event {
    private final long averageTimeBetweenGenerations;
    protected final Simulator simulator;
    protected final Random random;

    public AbstractGeneratorProcess(Simulator simulator, Random random, long averageTimeBetweenGenerations) {
        this.simulator = simulator;
        this.random = random;
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

    protected long timeToNextGeneration() {
        return random.sampleExponentialDistribution(averageTimeBetweenGenerations);
    }

    protected abstract void generate();
}
