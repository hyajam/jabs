package jabs.network.networks;

import jabs.network.networks.stats.MinerGlobalRegionDistribution;
import jabs.network.networks.stats.NetworkStats;
import jabs.network.networks.stats.NodeGlobalRegionDistribution;
import jabs.network.networks.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.ethereum.EthereumCasperMiner;
import jabs.network.node.nodes.ethereum.EthereumCasperNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class CasperFFGGlobalBlockchainNetwork<R extends Enum<R>> extends EthereumGlobalProofOfWorkNetwork<R> {
    protected final int checkpointSpace;

    public CasperFFGGlobalBlockchainNetwork(RandomnessEngine randomnessEngine, int checkpointSpace,
                                            ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
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

    public EthereumCasperNode createNewEthereumCasperNode(Simulator simulator, Network<R> network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        R region = sampleRegion();
        return new EthereumCasperNode(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), checkpointSpace, numOfStakeholders);
    }

    public EthereumCasperMiner createNewEthereumCasperMiner(Simulator simulator, Network<R> network, int nodeID, int checkpointSpace, int numOfStakeholders) {
        R region = sampleRegion();
        return new EthereumCasperMiner(simulator, network, nodeID, network.sampleDownloadBandwidth(region), network.sampleUploadBandwidth(region), sampleHashPower(), checkpointSpace, numOfStakeholders);
    }
}
