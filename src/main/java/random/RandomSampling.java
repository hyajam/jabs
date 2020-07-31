package main.java.random;

import main.java.Main;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.Collections;
import java.util.List;

public class RandomSampling {
    public static <E> List<E> sampleSubset(List<E> list, int n) {
        RandomGenerator rand = Main.random;
        int length = list.size();
        if (length < n) return null;
        for (int i = length - 1; i >= length - n; --i) {
            Collections.swap(list, i , rand.nextInt(i + 1));
        }
        return list.subList(length - n, length);
    }

    public static <E> E sampleFromList(List<E> list) {
        RandomGenerator rand = Main.random;
        return list.get(rand.nextInt(list.size()));
    }

    public static long sampleDistributionWithBins(double[] dist, long[] bins) {
        double rand = Main.random.nextDouble();
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
        double rand = Main.random.nextDouble();
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

}
