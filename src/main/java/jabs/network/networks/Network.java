package jabs.network.networks;

import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.network.stats.NetworkStats;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Network<N extends Node, NodeType extends Enum<NodeType>> {
    protected final List<N> nodes = new ArrayList<N>();
    protected final RandomnessEngine randomnessEngine;
    public final NetworkStats<NodeType> networkStats;
    public final HashMap<N, NodeType> nodeTypes = new HashMap<>();

    protected Network(RandomnessEngine randomnessEngine, NetworkStats<NodeType> networkStats) {
        this.randomnessEngine = randomnessEngine;
        this.networkStats = networkStats;
    }

    public N getRandomNode() {
        return nodes.get(randomnessEngine.sampleInt(nodes.size()));
    }

    public List<N> getAllNodes() {
        return nodes;
    }

    public N getNode(int i) {
        return nodes.get(i);
    }

    public double getLatency(N from, N to) {
        return networkStats.getLatency(nodeTypes.get(from), nodeTypes.get(to));
    };

    public long sampleDownloadBandwidth(NodeType type) {
        return networkStats.sampleDownloadBandwidth(type);
    };
    public long sampleUploadBandwidth(NodeType type){
        return networkStats.sampleUploadBandwidth(type);
    };

    public abstract void populateNetwork(Simulator simulator, ConsensusAlgorithmConfig consensusAlgorithmConfig);
    public abstract void populateNetwork(Simulator simulator, int numNodes,
                                         ConsensusAlgorithmConfig consensusAlgorithmConfig);
    public abstract void addNode(N node);

    public void addNode(N node, NodeType nodeType) {
        nodes.add(node);
        nodeTypes.put(node, nodeType);
    }

    public RandomnessEngine getRandom() {
        return this.randomnessEngine;
    }
}

