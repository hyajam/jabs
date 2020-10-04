package main.java.network;

import main.java.node.nodes.MinerNode;
import main.java.node.nodes.Node;

import java.util.ArrayList;
import java.util.List;

import static main.java.random.Random.sampleFromDistribution;
import static main.java.random.Random.sampleInt;

public abstract class Network {
    private final List<Node> nodes = new ArrayList<>();
    private final List<MinerNode> miners = new ArrayList<>();
    private long totalHashPower = 0;
    private final List<Long> minersHashPower = new ArrayList<>();

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void addMiner(MinerNode node){
        nodes.add((Node) node);
        miners.add(node);
        minersHashPower.add(node.getHashPower());
        totalHashPower += node.getHashPower();
    }

    public Node getRandomNode() {
        return nodes.get(sampleInt(nodes.size()));
    }

    public MinerNode getRandomMinerByHashPower() {
        double[] hashPowerDistribution = new double[miners.size()];

        for (int i = 0; i < miners.size(); i++) {
            hashPowerDistribution[i] = ((double) minersHashPower.get(i)) / ((double) totalHashPower);
        }

        return miners.get(sampleFromDistribution(hashPowerDistribution));
    }

    public List<Node> getAllNodes() {
        return nodes;
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }

    public List<MinerNode> getAllMiners() {
        return miners;
    }

    public MinerNode getMiner(int i) {
        return miners.get(i);
    }

    public long getTotalHashPower() {
        return totalHashPower;
    }

    public abstract long getLatency(int from, int to);

    public abstract long sampleDownloadBandwidth(int region);

    public abstract long sampleUploadBandwidth(int region);

    public void clear() {
        nodes.clear();
        miners.clear();
        totalHashPower = 0;
        minersHashPower.clear();
    }
}

