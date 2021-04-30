package jabs.network;

import jabs.node.nodes.Node;
import jabs.node.nodes.ethereum.EthereumCasperMiner;
import jabs.node.nodes.ethereum.EthereumCasperNode;
import jabs.random.Random;
import jabs.simulator.Simulator;

public class CasperFFGGlobalBlockchainNetwork extends EthereumGlobalBlockchainNetwork {
    protected final int checkpointSpace;

    public CasperFFGGlobalBlockchainNetwork(Random random, int checkpointSpace) {
        super(random);
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
        return new EthereumCasperNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), checkpointSpace, numOfStakeholders);
    }

    public EthereumCasperMiner createNewEthereumCasperMiner(Simulator simulator, Network network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        int region = sampleRegion();
        return new EthereumCasperMiner(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower(), checkpointSpace, numOfStakeholders);
    }
}
