package main.java.consensus;

import main.java.data.Block;
import main.java.data.Transaction;

public interface ConsensusAlgorithm<B extends Block<B>, T extends Transaction<T>> {

    void newBlock(B block);
    boolean isBlockAccepted(B block);
    boolean isTxAccepted(T tx);
    boolean isBlockValid(B block);
    int getNumOfAcceptedBlocks();
    int getNumOfAcceptedTxs();

}
