package main.java.random;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Collections;
import java.util.List;

public class Random {
    private final static RandomGenerator RANDOM_GENERATOR = new MersenneTwister();

    public static <E> List<E> sampleSubset(List<E> list, int n) {
        int length = list.size();
        if (length < n) return null;
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , RANDOM_GENERATOR.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public static <E> E sampleFromList(List<E> list) {
        return list.get(RANDOM_GENERATOR.nextInt(list.size()));
    }

    public static long sampleDistributionWithBins(double[] dist, long[] bins) {
        double rand = RANDOM_GENERATOR.nextDouble();
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

    public static int sampleFromDistribution(double[] dist) {
        double rand = RANDOM_GENERATOR.nextDouble();
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

    public static int sampleInt(int max) {
        return RANDOM_GENERATOR.nextInt(max);
    }

    public static double sampleDouble(double max) {
        return RANDOM_GENERATOR.nextDouble() * max;
    }

    public static long sampleExponentialDistribution(double averageTimeBetweenGenerations) {
        ExponentialDistribution expDist = new ExponentialDistribution(RANDOM_GENERATOR, averageTimeBetweenGenerations);
        return (long) expDist.sample();
    }

    public static long sampleParetoDistribution(double scale, double shape) {
        ParetoDistribution pareto = new ParetoDistribution(RANDOM_GENERATOR, scale, shape);
        return (long) pareto.sample();
    }

    public static void setSeed(long seed) {
        RANDOM_GENERATOR.setSeed(seed);
    }
}
