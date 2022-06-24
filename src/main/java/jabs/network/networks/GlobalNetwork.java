package jabs.network.networks;

import jabs.network.stats.NodeGlobalNetworkStats;
import jabs.network.stats.NodeGlobalRegionDistribution;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;

public abstract class GlobalNetwork<N extends Node, R extends Enum<R>> extends Network<N, R> {
    public final NodeGlobalRegionDistribution<R> nodeDistribution;

    public GlobalNetwork(RandomnessEngine randomnessEngine, NodeGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
        this.nodeDistribution = networkStats;
    }

    @Override
    public void addNode(N node) {
        this.addNode(node, sampleRegion());
    }

    public R sampleRegion() {
        return nodeDistribution.sampleRegion();
    }
}
