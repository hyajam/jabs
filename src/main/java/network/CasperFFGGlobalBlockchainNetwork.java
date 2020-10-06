package main.java.network;

import main.java.node.nodes.Node;
import main.java.node.nodes.ethereum.EthereumCasperMiner;
import main.java.node.nodes.ethereum.EthereumCasperNode;
import main.java.simulator.Simulator;

public class CasperFFGGlobalBlockchainNetwork extends EthereumGlobalBlockchainNetwork {
    protected final int checkpointSpace;

    public CasperFFGGlobalBlockchainNetwork(int checkpointSpace) {
        this.checkpointSpace = checkpointSpace;
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createNewEthereumCasperMiner(simulator, this, i, checkpointSpace, numNonMiners + numMiners));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createNewEthereumCasperNode(simulator, this, numMiners + i, checkpointSpace, numNonMiners + numMiners));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    public EthereumCasperNode createNewEthereumCasperNode(Simulator simulator, Network network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        int region = sampleRegion();
        return new EthereumCasperNode(simulator, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), checkpointSpace, numOfStakeholders);
    }

    public EthereumCasperMiner createNewEthereumCasperMiner(Simulator simulator, Network network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        int region = sampleRegion();
        return new EthereumCasperMiner(simulator, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower(), checkpointSpace, numOfStakeholders);
    }
}
