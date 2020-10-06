package main.java.network;

import main.java.node.nodes.Node;
import main.java.random.Random;

public abstract class LANNetwork extends Network {
    public static final double GLOBAL_LATENCY_BY_REGION = 2; // 2ms
    public static final double LATENCY_PARETO_SHAPE = 5;

    public LANNetwork(Random random) {
        super(random);
    }

    @Override
    public long getLatency(Random random, Node from, Node to) {
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * GLOBAL_LATENCY_BY_REGION;
        return random.sampleParetoDistribution(scale, LATENCY_PARETO_SHAPE);
    }

    @Override
    public long sampleDownloadBandwidth(int region) {
        return 10000000;
    }

    @Override
    public long sampleUploadBandwidth(int region) {
        return 10000000;
    }
}
