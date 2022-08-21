package jabs.consensus.config;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.SingleParentBlock;

import java.util.Objects;

/**
 */
public final class CasperFFGConfig<B extends SingleParentBlock<B>> extends GhostProtocolConfig<B> {
    private final int checkpointSpace;
    private final int numOfStakeholders;

    /**
     * @param checkpointSpace
     * @param numOfStakeholders
     */
    public CasperFFGConfig(B genesisBlock, double averageBlockMiningInterval, int checkpointSpace, int numOfStakeholders) {
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
