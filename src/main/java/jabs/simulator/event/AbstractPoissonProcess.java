package jabs.simulator.event;

import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class AbstractPoissonProcess implements Event {
    private final double averageTimeBetweenGenerations;
    protected final Simulator simulator;
    protected final RandomnessEngine randomnessEngine;

    public AbstractPoissonProcess(Simulator simulator, RandomnessEngine randomnessEngine, double averageTimeBetweenGenerations) {
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

    public double timeToNextGeneration() {
        return randomnessEngine.sampleExponentialDistribution(averageTimeBetweenGenerations);
    }

    protected abstract void generate();
}
