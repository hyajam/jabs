package jabs.consensus.config;

import jabs.ledgerdata.Block;

/**
 */
public class ChainBasedConsensusConfig
        implements ConsensusAlgorithmConfig {
    private final double averageBlockMiningInterval;

    /**
     * @param averageBlockMiningInterval
     */
    public ChainBasedConsensusConfig(double averageBlockMiningInterval) {
        this.averageBlockMiningInterval = averageBlockMiningInterval;
    }

    public double averageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }
}
