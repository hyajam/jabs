package jabs.consensus.config;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.tangle.TangleBlock;

import java.util.Objects;

/**
 * @param averageBlockMiningDifficulty
 */
public record TangleIOTAConsensusConfig(double averageBlockMiningDifficulty)
        implements ConsensusAlgorithmConfig {
}
