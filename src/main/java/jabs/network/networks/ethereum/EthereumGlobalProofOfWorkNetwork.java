package jabs.network.networks.ethereum;

import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.consensus.config.GhostProtocolConfig;
import jabs.ledgerdata.Block;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.stats.*;
import jabs.network.node.nodes.ethereum.EthereumMinerNode;
import jabs.network.node.nodes.ethereum.EthereumNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.HashSet;

public class EthereumGlobalProofOfWorkNetwork<R extends Enum<R>> extends
        GlobalProofOfWorkNetwork<EthereumNode, EthereumMinerNode, EthereumBlock, R> {
    public EthereumGlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine,
                                            ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    /**
     * @param difficulty Difficulty of genesis block
     * @return Parent-less block that could be used for genesis block
     */
    @Override
    public EthereumBlock genesisBlock(double difficulty) {
        return new EthereumBlock(0, 0, 0, null, null, new HashSet<>(), difficulty, 0);
    }

    @Override
    public EthereumNode createSampleNode(Simulator simulator, int nodeID, EthereumBlock genesisBlock,
                                         ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = this.sampleRegion();
        return new EthereumNode(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region),
                genesisBlock, (GhostProtocolConfig) chainBasedConsensusConfig);
    }

    @Override
    public EthereumMinerNode createSampleMiner(Simulator simulator, int nodeID, long hashPower,
                                               EthereumBlock genesisBlock,
                                               ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = this.sampleMinerRegion();
        return new EthereumMinerNode(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), hashPower,
                genesisBlock, (GhostProtocolConfig) chainBasedConsensusConfig);
    }

}
