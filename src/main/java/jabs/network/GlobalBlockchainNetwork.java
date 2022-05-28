package jabs.network;

import jabs.node.nodes.MinerNode;
import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class GlobalBlockchainNetwork extends BlockchainNetwork {
    public final HashMap<Node, Integer> nodeRegion = new HashMap<>();

    public static final List<String> REGION_LIST = new ArrayList<>(Arrays.asList("NORTH_AMERICA", "EUROPE", "SOUTH_AMERICA", "ASIA_PACIFIC", "JAPAN", "AUSTRALIA"));

    public static final double[][] DOWNLOAD_BANDWIDTH_DISTRIBUTION = {
            {0.13850, 0.05950, 0.09350, 0.12150, 0.14150, 0.20850, 0.15350, 0.06550, 0.01050, 0.00750},
            {0.10550, 0.06150, 0.08850, 0.14950, 0.19400, 0.17950, 0.13550, 0.06250, 0.02100, 0.00250},
            {0.29650, 0.13400, 0.15350, 0.18700, 0.13700, 0.05800, 0.02550, 0.00850, 0.00000, 0.00000},
            {0.19550, 0.12750, 0.17900, 0.18000, 0.15700, 0.08900, 0.04750, 0.02100, 0.00300, 0.00050},
            {0.25050, 0.10850, 0.14350, 0.11700, 0.09400, 0.11350, 0.06900, 0.09950, 0.00400, 0.00050},
            {0.18850, 0.10500, 0.12300, 0.18200, 0.16400, 0.12750, 0.05100, 0.05800, 0.00100, 0.00000},
    };

    public static final double[][] UPLOAD_BANDWIDTH_DISTRIBUTION = {
            {0.09750, 0.07350, 0.11550, 0.15750, 0.12200, 0.24000, 0.13800, 0.03850, 0.01100, 0.00650},
            {0.07500, 0.05900, 0.08800, 0.09500, 0.14850, 0.18900, 0.16850, 0.09900, 0.06250, 0.01550},
            {0.21900, 0.17350, 0.16150, 0.10450, 0.18750, 0.12000, 0.02550, 0.00450, 0.00350, 0.00050},
            {0.14350, 0.06850, 0.11150, 0.11800, 0.17050, 0.12500, 0.14200, 0.08000, 0.03650, 0.00450},
            {0.11200, 0.07450, 0.08450, 0.14900, 0.21600, 0.23100, 0.10250, 0.02500, 0.00550, 0.00000},
            {0.09650, 0.06950, 0.31000, 0.09550, 0.12600, 0.12650, 0.03000, 0.09850, 0.04550, 0.00200},
    };

    public static final long[] DOWNLOAD_BANDWIDTH_BIN = {
            600000, 1600000, 3600000, 7600000, 15600000, 31600000, 63600000, 127600000, 255600000, 499600000
    };

    public static final long[] UPLOAD_BANDWIDTH_BIN = {
            200000,  400000,  800000, 1600000,  3200000,  6400000, 12800000,  25600000,  51200000,  99900000
    };

    // GLOBAL_LATENCY_BY_REGION[i][j] is average latency from REGION_LIST[i] to REGION_LIST[j]
    // Unit: millisecond
    public static final double[][] GLOBAL_LATENCY_BY_REGION = {
            {0.032, 0.124, 0.184, 0.198, 0.151, 0.189},
            {0.124, 0.011, 0.227, 0.237, 0.252, 0.294},
            {0.184, 0.227, 0.088, 0.325, 0.301, 0.322},
            {0.198, 0.237, 0.325, 0.085, 0.058, 0.198},
            {0.151, 0.252, 0.301, 0.058, 0.012, 0.126},
            {0.189, 0.294, 0.322, 0.198, 0.126, 0.016}
    };

    public static final double LATENCY_PARETO_SHAPE = 5;

    public GlobalBlockchainNetwork(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    private long sampleBandwidthByRegion(int region, double[][] dist, long[] bins) {
        return randomnessEngine.sampleDistributionWithBins(dist[region], bins);
    }

    // latency by pareto distribution
    @Override
    public double getLatency(Node from, Node to) {
        double mean = GLOBAL_LATENCY_BY_REGION[nodeRegion.get(from)][nodeRegion.get(to)];
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * mean;
        return randomnessEngine.sampleParetoDistribution(scale, LATENCY_PARETO_SHAPE);
    }

    public long sampleDownloadBandwidth(int region) {
        return sampleBandwidthByRegion(region, DOWNLOAD_BANDWIDTH_DISTRIBUTION, DOWNLOAD_BANDWIDTH_BIN);
    }

    public long sampleUploadBandwidth(int region) {
        return sampleBandwidthByRegion(region, UPLOAD_BANDWIDTH_DISTRIBUTION, UPLOAD_BANDWIDTH_BIN);
    }

    @Override
    public void addNode(Node node) {
        nodes.add(node);
        nodeRegion.put(node, sampleRegion());
    }

    @Override
    public void addMiner(MinerNode node) {
        nodes.add((Node) node);
        miners.add(node);
        minersHashPower.add(node.getHashPower());
        totalHashPower += node.getHashPower();
        nodeRegion.put((Node) node, sampleMinerRegion());
    }

    public abstract int sampleRegion();
    public abstract int sampleMinerRegion();

    public abstract void populateNetwork(Simulator simulator, int numMiners, int numNonMiners);
}
