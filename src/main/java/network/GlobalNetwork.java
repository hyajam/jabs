package main.java.network;

import static main.java.config.NetworkStats.*;
import static main.java.random.Random.sampleDistributionWithBins;
import static main.java.random.Random.sampleParetoDistribution;

public class GlobalNetwork extends Network {

    // latency by pareto distribution
    public long getLatency(int from, int to) {
        long mean = main.java.config.NetworkStats.LATENCY[from][to];
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * (double)mean;
        return sampleParetoDistribution(scale, LATENCY_PARETO_SHAPE);
    }

    private long sampleBandwidthByRegion(int region, double[][] dist, long[] bins) {
        return sampleDistributionWithBins(dist[region], bins);
    }

    public long sampleDownloadBandwidth(int region) {
        return sampleBandwidthByRegion(region, DOWNLOAD_BANDWIDTH_DISTRIBUTION, DOWNLOAD_BANDWIDTH_BIN);
    }

    public long sampleUploadBandwidth(int region) {
        return sampleBandwidthByRegion(region, UPLOAD_BANDWIDTH_DISTRIBUTION, UPLOAD_BANDWIDTH_BIN);
    }

}
