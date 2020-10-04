package main.java.network;

import main.java.node.nodes.MinerNode;
import main.java.node.nodes.Node;

import java.util.ArrayList;
import java.util.List;

import static main.java.config.NetworkStats.*;
import static main.java.random.Random.*;

public abstract class Network {
    private static final List<Node> nodes = new ArrayList<>();
    private static final List<MinerNode> miners = new ArrayList<>();
    private static long totalHashPower = 0;
    private static final List<Long> minersHashPower = new ArrayList<>();

    public static void addNode(Node node) {
        nodes.add(node);
    }

    public static void addMiner(MinerNode node){
        nodes.add((Node) node);
        miners.add(node);
        minersHashPower.add(node.getHashPower());
        totalHashPower += node.getHashPower();
    }

    public static Node sampleNode() {
        return nodes.get(sampleInt(nodes.size()));
    }

    public static MinerNode sampleMinerByHashPower() {
        double[] hashPowerDistribution = new double[miners.size()];

        for (int i = 0; i < miners.size(); i++) {
            hashPowerDistribution[i] = ((double) minersHashPower.get(i)) / ((double) totalHashPower);
        }

        return miners.get(sampleFromDistribution(hashPowerDistribution));
    }

    public static List<Node> getAllNodes() {
        return nodes;
    }

    public static Node getNode(int i) {
        return nodes.get(i);
    }

    public static List<MinerNode> getAllMiners() {
        return miners;
    }

    public static MinerNode getMiner(int i) {
        return miners.get(i);
    }

    public static long getTotalHashPower() {
        return totalHashPower;
    }

    // latency by pareto distribution
    public static long getLatency(int from, int to) {
        long mean = main.java.config.NetworkStats.LATENCY[from][to];
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * (double)mean;
        return sampleParetoDistribution(scale, LATENCY_PARETO_SHAPE);
    }

    private static long sampleBandwidthByRegion(int region, double[][] dist, long[] bins) {
        return sampleDistributionWithBins(dist[region], bins);
    }

    public static long sampleDownloadBandwidth(int region) {
        return sampleBandwidthByRegion(region, DOWNLOAD_BANDWIDTH_DISTRIBUTION, DOWNLOAD_BANDWIDTH_BIN);
    }

    public static long sampleUploadBandwidth(int region) {
        return sampleBandwidthByRegion(region, UPLOAD_BANDWIDTH_DISTRIBUTION, UPLOAD_BANDWIDTH_BIN);
    }

    public static void clear() {
        nodes.clear();
        miners.clear();
        totalHashPower = 0;
        minersHashPower.clear();
    }
}

