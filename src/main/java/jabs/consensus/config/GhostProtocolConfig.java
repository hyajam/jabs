package jabs.consensus.config;

import jabs.ledgerdata.Block;

import java.util.Objects;

/**
 */
public class GhostProtocolConfig extends ChainBasedConsensusConfig {
    /**
     * @param averageBlockMiningInterval
     */
    public GhostProtocolConfig(double averageBlockMiningInterval) {
        super(averageBlockMiningInterval);
    }
}
