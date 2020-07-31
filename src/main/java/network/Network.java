package main.java.network;

import main.java.Main;
import main.java.node.nodes.MinerNode;
import main.java.node.nodes.Node;
import org.apache.commons.math3.distribution.ParetoDistribution;

import java.util.ArrayList;
import java.util.List;

import static main.java.config.NetworkStats.*;
import static main.java.random.RandomSampling.sampleDistributionWithBins;

public abstract class Network {
    private static final List<Node> nodes = new ArrayList<>();
    private static final List<MinerNode> miners = new ArrayList<>();
    private static long totalHashPower = 0;

    public static void addNode(Node node){
        nodes.add(node);
    }

    public static void addMiner(MinerNode node){
        nodes.add((Node) node);
        miners.add(node);
        totalHashPower += node.getHashPower();
    }

    public static List<Node> getAllNodes(){
        return nodes;
    }

    public static List<MinerNode> getAllMiners() {
        return miners;
    }

    public static long getTotalHashPower() {
        return totalHashPower;
    }

    // latency by pareto dist
    public static long getLatency(int from, int to){
        long mean = main.java.config.NetworkStats.LATENCY[from][to];
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * (double)mean;
        ParetoDistribution pareto = new ParetoDistribution(Main.random, scale, LATENCY_PARETO_SHAPE);
        return (long) pareto.sample();
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
}

