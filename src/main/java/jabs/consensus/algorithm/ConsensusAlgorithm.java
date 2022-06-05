package jabs.consensus.algorithm;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;

public interface ConsensusAlgorithm<B extends Block<B>, T extends Tx<T>> {

    void newIncomingBlock(B block);
    boolean isBlockConfirmed(B block);
    boolean isTxConfirmed(T tx);
    boolean isBlockValid(B block);
    int getNumOfAcceptedBlocks();
    int getNumOfAcceptedTxs();

}
