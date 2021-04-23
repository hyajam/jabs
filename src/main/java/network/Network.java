package main.java.network;

import main.java.node.nodes.Node;
import main.java.random.Random;
import main.java.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;

public abstract class Network {
    protected final List<Node> nodes = new ArrayList<>();
    protected final Random random;

    protected Network(Random random) {
        this.random = random;
    }

    public Node getRandomNode() {
        return nodes.get(random.sampleInt(nodes.size()));
    }

    public List<Node> getAllNodes() {
        return nodes;
    }

    public Node getNode(int i) {
        return nodes.get(i);
    }

    public abstract long getLatency(Node from, Node to);

    public abstract long sampleDownloadBandwidth(int region);

    public abstract long sampleUploadBandwidth(int region);

    public abstract void populateNetwork(Simulator simulator);

    public void addNode(Node node) {
        nodes.add(node);
    }

    public Random getRandom() {
        return this.random;
    }
}

