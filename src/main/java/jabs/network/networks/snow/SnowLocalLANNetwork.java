package jabs.network.networks.snow;

import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.snow.SnowNode;
import jabs.network.stats.lan.LAN100MNetworkStats;
import jabs.network.stats.lan.SingleNodeType;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

public class SnowLocalLANNetwork extends Network<SnowNode, SingleNodeType> {
    public SnowLocalLANNetwork(RandomnessEngine randomnessEngine) {
        super(randomnessEngine, new LAN100MNetworkStats(randomnessEngine));
    }

    public SnowNode createNewSnowNode(Simulator simulator, int nodeID, int numAllParticipants) {
        return new SnowNode(simulator, this, nodeID,
                this.sampleDownloadBandwidth(SingleNodeType.LAN_NODE),
                this.sampleUploadBandwidth(SingleNodeType.LAN_NODE),
                numAllParticipants);
    }

    @Override
    public void populateNetwork(Simulator simulator, ConsensusAlgorithmConfig snowConsensusConfig) {
        populateNetwork(simulator, 40, snowConsensusConfig);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes, ConsensusAlgorithmConfig snowConsensusConfig) {
        for (int i = 0; i < numNodes; i++) {
            this.addNode(createNewSnowNode(simulator, i, numNodes), SingleNodeType.LAN_NODE);
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    /**
     * @param node A Snow node to add to the network
     */
    @Override
    public void addNode(SnowNode node) {
        this.addNode(node, SingleNodeType.LAN_NODE);
    }
}
