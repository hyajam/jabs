package jabs.network;

import jabs.node.nodes.Node;
import jabs.node.nodes.ethereum.EthereumDAGsperMiner;
import jabs.node.nodes.ethereum.EthereumDAGsperNode;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class DAGsperGlobalBlockchainNetwork extends EthereumGlobalBlockchainNetwork {
    private final int checkpointSpace;

    public DAGsperGlobalBlockchainNetwork(RandomnessEngine randomnessEngine, int checkpointSpace) {
        super(randomnessEngine);
        this.checkpointSpace = checkpointSpace;
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createNewEthereumDAGsperMiner(simulator, this, i, checkpointSpace, numNonMiners + numMiners));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createNewEthereumDAGsperNode(simulator, this, numMiners + i, checkpointSpace, numNonMiners + numMiners));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    public EthereumDAGsperNode createNewEthereumDAGsperNode(Simulator simulator, Network network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        int region = sampleRegion();
        return new EthereumDAGsperNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), checkpointSpace, numOfStakeholders);
    }

    public EthereumDAGsperMiner createNewEthereumDAGsperMiner(Simulator simulator, Network network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        int region = sampleMinerRegion();
        return new EthereumDAGsperMiner(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower(), checkpointSpace, numOfStakeholders);
    }
}
