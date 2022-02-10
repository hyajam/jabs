package jabs.randengine;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Collections;
import java.util.List;

public class RandomnessEngine extends MersenneTwister {
    public RandomnessEngine(long seed) {
        super(seed);
    }

    public <E> List<E> sampleSubset(List<E> list, int n) {
        int length = list.size();
        if (length < n) return null;
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , this.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public <E> E sampleFromList(List<E> list) {
        return list.get(this.nextInt(list.size()));
    }

    public long sampleDistributionWithBins(double[] dist, long[] bins) {
        double rand = this.nextDouble();
        for (int i = 0; i < dist.length-1; i++) {
            if (rand < dist[i]) {
                double diff = rand / dist[i];
                return (bins[i] + (long)(diff * (bins[i+1]-bins[i])));
            } else {
                rand -= dist[i];
            }
        }
        return bins[bins.length-1];
    }

    public int sampleFromDistribution(double[] dist) {
        double rand = this.nextDouble();
        for (int i = 0; i < dist.length-1; i++) {
            if (rand < dist[i]) {
                double diff = rand / dist[i];
                return i;
            } else {
                rand -= dist[i];
            }
        }
        return dist.length-1;
    }

    public int sampleInt(int max) {
        return this.nextInt(max);
    }

    public double sampleDouble(double max) {
        return this.nextDouble() * max;
    }

    public long sampleExponentialDistribution(double averageTimeBetweenGenerations) {
        ExponentialDistribution expDist = new ExponentialDistribution(this, averageTimeBetweenGenerations);
        return (long) expDist.sample();
    }

    public long sampleParetoDistribution(double scale, double shape) {
        ParetoDistribution pareto = new ParetoDistribution(this, scale, shape);
        return (long) pareto.sample();
    }
}
