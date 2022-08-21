package jabs.consensus.config;

import jabs.ledgerdata.SingleParentBlock;

/**
 */
public class ChainBasedConsensusConfig<B extends SingleParentBlock<B>>
        implements ConsensusAlgorithmConfig {
    private final double averageBlockMiningInterval;
    private final B genesisBlock;

    /**
     * @param averageBlockMiningInterval
     * @param genesisBlock
     */
    public ChainBasedConsensusConfig(B genesisBlock, double averageBlockMiningInterval) {
        this.averageBlockMiningInterval = averageBlockMiningInterval;
        this.genesisBlock = genesisBlock;
    }

    public double averageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }

    public B getGenesisBlock() {
        return this.genesisBlock;
    }
}
