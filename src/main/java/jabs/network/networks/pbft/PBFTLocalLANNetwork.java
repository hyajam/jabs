package jabs.network.networks.pbft;

import jabs.consensus.algorithm.PBFT;
import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.consensus.config.PBFTConsensusConfig;
import jabs.network.networks.Network;
import jabs.network.stats.lan.LAN100MNetworkStats;
import jabs.network.stats.lan.SingleNodeType;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.pbft.PBFTNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class PBFTLocalLANNetwork extends Network<PBFTNode, SingleNodeType> {
    public PBFTLocalLANNetwork(RandomnessEngine randomnessEngine) {
        super(randomnessEngine, new LAN100MNetworkStats(randomnessEngine));
    }

    public PBFTNode createNewPBFTNode(Simulator simulator, int nodeID, int numAllParticipants) {
        return new PBFTNode(simulator, this, nodeID,
                this.sampleDownloadBandwidth(SingleNodeType.LAN_NODE),
                this.sampleUploadBandwidth(SingleNodeType.LAN_NODE),
                numAllParticipants);
    }


    @Override
    public void populateNetwork(Simulator simulator, ConsensusAlgorithmConfig pbftConsensusConfig) {
        populateNetwork(simulator, 40, pbftConsensusConfig);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes, ConsensusAlgorithmConfig pbftConsensusConfig) {
        for (int i = 0; i < numNodes; i++) {
            this.addNode(createNewPBFTNode(simulator, i, numNodes), SingleNodeType.LAN_NODE);
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    /**
     * @param node A PBFT node to add to the network
     */
    @Override
    public void addNode(PBFTNode node) {
        this.addNode(node, SingleNodeType.LAN_NODE);
    }

}
