package jabs.consensus;

import jabs.data.Block;
import jabs.data.Tx;

public interface DeterministicFinalityConsensus<B extends Block<B>, T extends Tx<T>> {
    boolean isBlockFinalized(B block);
    boolean isTxFinalized(T tx);
    int getNumOfFinalizedBlocks();
    int getNumOfFinalizedTxs();
}
