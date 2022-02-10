package jabs.event;

import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractGeneratorProcess implements Event {
    private final long averageTimeBetweenGenerations;
    protected final Simulator simulator;
    protected final RandomnessEngine randomnessEngine;

    public AbstractGeneratorProcess(Simulator simulator, RandomnessEngine randomnessEngine, long averageTimeBetweenGenerations) {
        this.simulator = simulator;
        this.randomnessEngine = randomnessEngine;
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
        return randomnessEngine.sampleExponentialDistribution(averageTimeBetweenGenerations);
    }

    protected abstract void generate();
}
