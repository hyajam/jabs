package jabs.network.networks;

import jabs.network.networks.stats.NodeGlobalNetworkStats;
import jabs.network.networks.stats.NodeGlobalRegionDistribution;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public abstract class GlobalNetwork<Region extends Enum<Region>> extends Network<Region> {
    public final NodeGlobalRegionDistribution<Region> nodeDistribution;

    public GlobalNetwork(RandomnessEngine randomnessEngine, NodeGlobalNetworkStats<Region> networkStats) {
        super(randomnessEngine, networkStats);
        this.nodeDistribution = networkStats;
    }

    @Override
    public void addNode(Node node) {
        this.addNode(node, sampleRegion());
    }

    public Region sampleRegion() {
        return nodeDistribution.sampleRegion();
    }
}
