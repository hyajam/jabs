package jabs.consensus.config;

import jabs.ledgerdata.SingleParentBlock;

/**
 */
public class NakamotoConsensusConfig<B extends SingleParentBlock<B>>
        extends ChainBasedConsensusConfig<B> {
    private final int confirmationDepth;
    /**
     * @param averageBlockMiningInterval
     * @param confirmationDepth
     */
    public NakamotoConsensusConfig(B genesisBlock, double averageBlockMiningInterval, int confirmationDepth) {
        super(genesisBlock, averageBlockMiningInterval);
        this.confirmationDepth = confirmationDepth;
    }

    public int getConfirmationDepth() {
        return confirmationDepth;
    }
}
