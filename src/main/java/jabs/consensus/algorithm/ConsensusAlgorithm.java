package jabs.consensus.algorithm;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;

/**
 * @param <B>
 * @param <T>
 */
public interface ConsensusAlgorithm<B extends Block<B>, T extends Tx<T>> {

    /**
     * @param block
     */
    void newIncomingBlock(B block);

    /**
     * @param block
     * @return
     */
    boolean isBlockConfirmed(B block);

    /**
     * @param tx
     * @return
     */
    boolean isTxConfirmed(T tx);

    /**
     * @param block
     * @return
     */
    boolean isBlockValid(B block);

    /**
     * @return
     */
    int getNumOfConfirmedBlocks();

    /**
     * @return
     */
    int getNumOfConfirmedTxs();

}
