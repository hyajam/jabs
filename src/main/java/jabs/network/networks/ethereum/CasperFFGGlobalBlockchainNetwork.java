package jabs.network.networks.ethereum;

import jabs.consensus.config.CasperFFGConfig;
import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.networks.Network;
import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.ethereum.EthereumCasperMiner;
import jabs.network.node.nodes.ethereum.EthereumCasperNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class CasperFFGGlobalBlockchainNetwork<R extends Enum<R>> extends EthereumGlobalProofOfWorkNetwork<R> {
    protected final int checkpointSpace;
    protected static final double CASPER_DEFAULT_CHECKPOINT_SPACE = 14;

    public CasperFFGGlobalBlockchainNetwork(RandomnessEngine randomnessEngine, int checkpointSpace,
                                            ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
        this.checkpointSpace = checkpointSpace;
    }

    @Override
    public EthereumCasperNode createSampleNode(Simulator simulator, int nodeID, EthereumBlock genesisBlock,
                                               ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = (R) sampleRegion();
        return new EthereumCasperNode(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), genesisBlock,
                (CasperFFGConfig) chainBasedConsensusConfig);
    }

    @Override
    public EthereumCasperMiner createSampleMiner(Simulator simulator, int nodeID, double hashPower,
                                                 EthereumBlock genesisBlock,
                                                 ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = (R) sampleRegion();
        return new EthereumCasperMiner(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), hashPower, genesisBlock, (CasperFFGConfig) chainBasedConsensusConfig);
    }
}
