package main.java.event;

import main.java.Main;
import main.java.simulator.AbstractSimulator;
import org.apache.commons.math3.distribution.ExponentialDistribution;

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
            AbstractSimulator.putEvent(this, this.timeToNextGeneration());
        } else if (this.remainingNumberOfTxs == 0) {
            this.generate();
        } else {
            this.generate();
            AbstractSimulator.putEvent(this, this.timeToNextGeneration());
        }
    }

    protected long timeToNextGeneration() {
        ExponentialDistribution expDist = new ExponentialDistribution(Main.random, averageTimeBetweenGenerations);
        return (long) expDist.sample();
    }

    protected abstract void generate();
}
