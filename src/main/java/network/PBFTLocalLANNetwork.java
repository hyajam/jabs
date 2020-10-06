package main.java.network;

import main.java.node.nodes.Node;
import main.java.node.nodes.pbft.PBFTNode;
import main.java.simulator.Simulator;

public class PBFTLocalLANNetwork extends LANNetwork {
    public PBFTNode createNewPBFTNode(Simulator simulator, int nodeID, int numAllParticipants) {
        return new PBFTNode(simulator, nodeID, this.sampleDownloadBandwidth(0), this.sampleUploadBandwidth(0), numAllParticipants);
    }


    @Override
    public void populateNetwork(Simulator simulator) {
        populateNetwork(simulator, 40);
    }

    public void populateNetwork(Simulator simulator, int numNodes) {
        for (int i = 0; i < numNodes; i++) {
            this.addNode(createNewPBFTNode(simulator, i, numNodes));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

}
