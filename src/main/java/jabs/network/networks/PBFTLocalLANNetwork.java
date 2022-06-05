package jabs.network.networks;

import jabs.network.networks.stats.NetworkStats;
import jabs.network.networks.stats.lan.LAN100MNetworkStats;
import jabs.network.networks.stats.lan.SingleNodeType;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.pbft.PBFTNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class PBFTLocalLANNetwork extends Network<SingleNodeType> {
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
    public void populateNetwork(Simulator simulator) {
        populateNetwork(simulator, 40);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        for (int i = 0; i < numNodes; i++) {
            this.addNode(createNewPBFTNode(simulator, i, numNodes));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    /**
     * @param node
     */
    @Override
    public void addNode(Node node) {
        this.addNode(node, SingleNodeType.LAN_NODE);
    }

}
