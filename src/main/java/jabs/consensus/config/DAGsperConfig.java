package jabs.consensus.config;

import jabs.ledgerdata.Block;

import java.util.Objects;

/**
 *
 */
public final class DAGsperConfig extends GhostProtocolConfig {
    private final int checkpointSpace;
    private final int numOfStakeholders;

    /**
     * @param genesisBlock
     * @param checkpointSpace
     * @param numOfStakeholders
     */
    public DAGsperConfig(Block genesisBlock, double averageBlockMiningInterval, int checkpointSpace, int numOfStakeholders) {
        super(averageBlockMiningInterval);
        this.checkpointSpace = checkpointSpace;
        this.numOfStakeholders = numOfStakeholders;
    }

    public int checkpointSpace() {
        return checkpointSpace;
    }

    public int numOfStakeholders() {
        return numOfStakeholders;
    }
}
