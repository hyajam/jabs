package jabs.network.networks.bitcoin;

import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNodeWithoutTx;
import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BitcoinGlobalProofOfWorkNetworkWithoutTx<R extends Enum<R>> extends BitcoinGlobalProofOfWorkNetwork<R> {
    public BitcoinGlobalProofOfWorkNetworkWithoutTx(RandomnessEngine randomnessEngine,
                                                    ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    @Override
    public BitcoinMinerNode createSampleMiner(Simulator simulator, int nodeID, double hashPower,
                                              BitcoinBlockWithoutTx genesisBlock,
                                              ChainBasedConsensusConfig chainBasedConsensusConfig) {
        R region = this.sampleRegion();
        return new BitcoinMinerNodeWithoutTx(simulator, this, nodeID, this.sampleDownloadBandwidth(region),
                this.sampleUploadBandwidth(region), hashPower, genesisBlock, chainBasedConsensusConfig);
    }
}
