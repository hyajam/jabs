package jabs.network.networks;

import jabs.network.networks.stats.MinerGlobalRegionDistribution;
import jabs.network.networks.stats.NetworkStats;
import jabs.network.networks.stats.NodeGlobalRegionDistribution;
import jabs.network.networks.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.ethereum.EthereumDAGsperMiner;
import jabs.network.node.nodes.ethereum.EthereumDAGsperNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class DAGsperGlobalBlockchainNetwork<R extends Enum<R>> extends EthereumGlobalProofOfWorkNetwork<R> {
    private final int checkpointSpace;

    public DAGsperGlobalBlockchainNetwork(RandomnessEngine randomnessEngine, int checkpointSpace,
                                          ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
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

    public EthereumDAGsperNode createNewEthereumDAGsperNode(Simulator simulator, Network<R> network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        R region = sampleRegion();
        return new EthereumDAGsperNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), checkpointSpace, numOfStakeholders);
    }

    public EthereumDAGsperMiner createNewEthereumDAGsperMiner(Simulator simulator, Network<R> network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        R region = sampleMinerRegion();
        return new EthereumDAGsperMiner(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower(), checkpointSpace, numOfStakeholders);
    }
}
