package jabs.consensus.algorithm;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;

public interface DeterministicFinalityConsensus<B extends Block<B>, T extends Tx<T>> {
    boolean isBlockFinalized(B block);
    boolean isTxFinalized(T tx);
    int getNumOfFinalizedBlocks();
    int getNumOfFinalizedTxs();
}
