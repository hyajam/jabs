package jabs.consensus.config;

/**
 */
public class NakamotoConsensusConfig
        extends ChainBasedConsensusConfig {
    private final int confirmationDepth;
    /**
     * @param averageBlockMiningInterval
     * @param confirmationDepth
     */
    public NakamotoConsensusConfig(double averageBlockMiningInterval, int confirmationDepth) {
        super(averageBlockMiningInterval);
        this.confirmationDepth = confirmationDepth;
    }

    public int getConfirmationDepth() {
        return confirmationDepth;
    }
}
