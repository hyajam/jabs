package jabs.consensus.config;

import jabs.ledgerdata.SingleParentBlock;

/**
 *
 */
public final class DAGsperConfig<B extends SingleParentBlock<B>> extends GhostProtocolConfig<B> {
    private final int checkpointSpace;
    private final int numOfStakeholders;

    /**
     * @param genesisBlock
     * @param checkpointSpace
     * @param numOfStakeholders
     */
    public DAGsperConfig(B genesisBlock, double averageBlockMiningInterval, int checkpointSpace,
                         int numOfStakeholders) {
        super(genesisBlock, averageBlockMiningInterval);
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
