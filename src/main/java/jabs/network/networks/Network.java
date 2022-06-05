package jabs.network.networks;

import jabs.network.networks.stats.NetworkStats;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Network<NodeType extends Enum<NodeType>> {
    protected final List<Node> nodes = new ArrayList<Node>();
    protected final RandomnessEngine randomnessEngine;
    public final NetworkStats<NodeType> networkStats;
    public final HashMap<Node, NodeType> nodeTypes = new HashMap<>();

    protected Network(RandomnessEngine randomnessEngine, NetworkStats<NodeType> networkStats) {
        this.randomnessEngine = randomnessEngine;
        this.networkStats = networkStats;
    }

    public Node getRandomNode() {
        return nodes.get(randomnessEngine.sampleInt(nodes.size()));
    }

    public List<Node> getAllNodes() {
        return nodes;
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }

    public double getLatency(Node from, Node to) {
        return networkStats.getLatency(nodeTypes.get(from), nodeTypes.get(to));
    };

    public long sampleDownloadBandwidth(NodeType type) {
        return networkStats.sampleDownloadBandwidth(type);
    };

    public long sampleUploadBandwidth(NodeType type){
        return networkStats.sampleUploadBandwidth(type);
    };

    public abstract void populateNetwork(Simulator simulator);

    public abstract void populateNetwork(Simulator simulator, int numNodes);

    public abstract void addNode(Node node);

    public void addNode(Node node, NodeType nodeType) {
        nodes.add(node);
        nodeTypes.put(node, nodeType);
    }

    public RandomnessEngine getRandom() {
        return this.randomnessEngine;
    }
}

