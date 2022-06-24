package jabs.network.networks.ethereum;

import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.consensus.config.DAGsperConfig;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.networks.Network;
import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
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


    public EthereumDAGsperNode createNewEthereumDAGsperNode(Simulator simulator, int nodeID, EthereumBlock genesisBlock,
                                                            ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = sampleRegion();
        return new EthereumDAGsperNode(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), genesisBlock, (DAGsperConfig) chainBasedConsensusConfig);
    }

    public EthereumDAGsperMiner createNewEthereumDAGsperMiner(Simulator simulator, int nodeID,
                                                              EthereumBlock genesisBlock,
                                                              ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = sampleMinerRegion();
        return new EthereumDAGsperMiner(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), sampleHashPower(), genesisBlock, (DAGsperConfig) chainBasedConsensusConfig);
    }
}
