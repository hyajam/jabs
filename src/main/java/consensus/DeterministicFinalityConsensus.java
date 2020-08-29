package main.java.consensus;

import main.java.data.Block;
import main.java.data.Tx;

public interface DeterministicFinalityConsensus<B extends Block<B>, T extends Tx<T>> {
    boolean isBlockFinalized(B block);
    boolean isTxFinalized(T tx);
    int getNumOfFinalizedBlocks();
    int getNumOfFinalizedTxs();
}
