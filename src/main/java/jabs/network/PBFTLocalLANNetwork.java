package jabs.network;

import jabs.node.nodes.Node;
import jabs.node.nodes.pbft.PBFTNode;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class PBFTLocalLANNetwork extends LANBlockchainNetwork {
    public PBFTLocalLANNetwork(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    @Override
    protected long sampleHashPower() {
        return 0;
    }

    public PBFTNode createNewPBFTNode(Simulator simulator, int nodeID, int numAllParticipants) {
        return new PBFTNode(simulator, this, nodeID, this.sampleDownloadBandwidth(0), this.sampleUploadBandwidth(0), numAllParticipants);
    }

    private static final long[] BITCOIN_BLOCK_SIZE_2020_BINS = {
            196, 119880, 254789, 396047, 553826, 726752, 917631, 1021479, 1054560, 1084003, 1113136, 1138722, 1161695,
            1183942, 1205734, 1227090, 1248408, 1270070, 1293647, 1320186, 1354939, 1423459, 2422858
    };

    private static final double[] BITCOIN_BLOCK_SIZE_2020 = {
            0.0000, 0.0482, 0.0422, 0.0422, 0.0421, 0.0422, 0.0421, 0.0445, 0.0455, 0.0458, 0.0461, 0.0468, 0.0472,
            0.0481, 0.0477, 0.0479, 0.0484, 0.0482, 0.0475, 0.0464, 0.0454, 0.0434, 0.0420
    };


    public int sampleBlockSize() {
        return (int) randomnessEngine.sampleDistributionWithBins(BITCOIN_BLOCK_SIZE_2020, BITCOIN_BLOCK_SIZE_2020_BINS);
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

}
