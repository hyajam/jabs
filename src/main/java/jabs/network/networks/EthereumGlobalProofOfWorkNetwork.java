package jabs.network.networks;

import jabs.network.networks.stats.*;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.ethereum.EthereumMinerNode;
import jabs.network.node.nodes.ethereum.EthereumNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class EthereumGlobalProofOfWorkNetwork<R extends Enum<R>> extends GlobalProofOfWorkNetwork<R> {
    public EthereumGlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine,
                                            ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    @Override
    public void populateNetwork(Simulator simulator) {
        populateNetwork(simulator, minerDistribution.totalNumberOfMiners(), nodeDistribution.totalNumberOfNodes());
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createNewEthereumMiner(simulator, this, i));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createNewEthereumNode(simulator, this, numMiners + i));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }

    public EthereumNode createNewEthereumNode(Simulator simulator, Network<R> network, int nodeID) {
        R region = this.sampleRegion();
        return new EthereumNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region));
    }

    public EthereumMinerNode createNewEthereumMiner(Simulator simulator, Network<R> network, int nodeID) {
        R region = this.sampleMinerRegion();
        return new EthereumMinerNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower());
    }

}
