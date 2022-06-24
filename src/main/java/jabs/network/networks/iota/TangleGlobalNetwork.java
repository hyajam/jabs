package jabs.network.networks.iota;

import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.consensus.config.TangleIOTAConsensusConfig;
import jabs.network.networks.GlobalNetwork;
import jabs.network.stats.NodeGlobalNetworkStats;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.iota.IOTANode;
import jabs.network.p2p.BitcoinCoreP2P;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

public class TangleGlobalNetwork<R extends Enum<R>> extends GlobalNetwork<IOTANode, R> {
    public TangleGlobalNetwork(RandomnessEngine randomnessEngine, NodeGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }


    public IOTANode createNewIOTANode(Simulator simulator, int nodeID,
                                      TangleIOTAConsensusConfig tangleIOTAConsensusConfig) {
        R nodeRegion = this.sampleRegion();
        return new IOTANode(simulator, this, nodeID,
                this.sampleDownloadBandwidth(nodeTypes.get(nodeRegion)),
                this.sampleUploadBandwidth(nodeTypes.get(nodeRegion)),
                new BitcoinCoreP2P(), tangleIOTAConsensusConfig); // TODO: New P2P model is required
    }

    @Override
    public R sampleRegion() {
        return nodeDistribution.sampleRegion();
    }


    @Override
    public void populateNetwork(Simulator simulator, ConsensusAlgorithmConfig consensusAlgorithmConfig) {
        this.populateNetwork(simulator, nodeDistribution.totalNumberOfNodes(), consensusAlgorithmConfig);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes, ConsensusAlgorithmConfig consensusAlgorithmConfig) {
        for (int i = 0; i < numNodes; i++) {
            this.addNode(createNewIOTANode(simulator, i, (TangleIOTAConsensusConfig) consensusAlgorithmConfig));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }
}
