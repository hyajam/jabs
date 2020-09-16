package main.java.event;

import main.java.simulator.Simulator;

import static main.java.random.Random.sampleExponentialDistribution;

public abstract class AbstractGeneratorProcess implements Event {
    private final long averageTimeBetweenGenerations;
    private int remainingNumberOfTxs;

    public AbstractGeneratorProcess(long averageTimeBetweenGenerations, int maxNumOfGeneration) {
        this.averageTimeBetweenGenerations = averageTimeBetweenGenerations;
        this.remainingNumberOfTxs = maxNumOfGeneration-1;
    }

    @Override
    public void execute() {
        this.generateAndSetNextEvent();
    }

    protected void generateAndSetNextEvent() {
        if (this.remainingNumberOfTxs > 0) {
            this.remainingNumberOfTxs--;
            this.generate();
            Simulator.putEvent(this, this.timeToNextGeneration());
        } else if (this.remainingNumberOfTxs == 0) {
            this.generate();
        } else {
            this.generate();
            Simulator.putEvent(this, this.timeToNextGeneration());
        }
    }

    protected long timeToNextGeneration() {
        return sampleExponentialDistribution(averageTimeBetweenGenerations);
    }

    protected abstract void generate();
}
