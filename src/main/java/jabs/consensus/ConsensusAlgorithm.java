package jabs.consensus;

import jabs.data.Block;
import jabs.data.Tx;

public interface ConsensusAlgorithm<B extends Block<B>, T extends Tx<T>> {

    void newIncomingBlock(B block);
    boolean isBlockAccepted(B block);
    boolean isTxAccepted(T tx);
    boolean isBlockValid(B block);
    int getNumOfAcceptedBlocks();
    int getNumOfAcceptedTxs();

}
