package jabs.consensus.config;

import jabs.ledgerdata.Block;

/**
 */
public class NakamotoConsensusConfig
        extends ChainBasedConsensusConfig {
    /**
     * @param averageBlockMiningInterval
     */
    public NakamotoConsensusConfig(double averageBlockMiningInterval) {
        super(averageBlockMiningInterval);
    }
}
