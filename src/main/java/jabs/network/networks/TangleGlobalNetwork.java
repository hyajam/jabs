package jabs.network.networks;

import jabs.network.networks.stats.NodeGlobalNetworkStats;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.iota.IOTANode;
import jabs.network.p2p.BitcoinCoreP2P;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

public class TangleGlobalNetwork<R extends Enum<R>> extends GlobalNetwork<R> {
    public TangleGlobalNetwork(RandomnessEngine randomnessEngine, NodeGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }


    public IOTANode createNewIOTANode(Simulator simulator, int nodeID) {
        R nodeRegion = this.sampleRegion();
        return new IOTANode(simulator, this, nodeID,
                this.sampleDownloadBandwidth(nodeTypes.get(nodeRegion)),
                this.sampleUploadBandwidth(nodeTypes.get(nodeRegion)),
                new BitcoinCoreP2P()); // TODO: New P2P model is required
    }

    @Override
    public R sampleRegion() {
        return nodeDistribution.sampleRegion();
    }


    @Override
    public void populateNetwork(Simulator simulator) {
        this.populateNetwork(simulator, nodeDistribution.totalNumberOfNodes());
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        for (int i = 0; i < numNodes; i++) {
            this.addNode(createNewIOTANode(simulator, i));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }
}
