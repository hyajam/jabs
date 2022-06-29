package jabs.consensus.algorithm;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;

import java.util.HashSet;

/**
 * @param <B>
 * @param <T>
 */
public abstract class AbstractConsensusAlgorithm<B extends Block<B>, T extends Tx<T>>
        implements ConsensusAlgorithm<B, T> {

    /**
     * All accepted blocks (received and agreed) for the consensus algorithm
     */
    protected HashSet<B> confirmedBlocks = new HashSet<>();

    /**
     * All accepted transactions (residing inside accepted blocks)
     */
    protected final HashSet<T> confirmedTxs = new HashSet<>();

    /**
     * When a new block is received by the node this function should be called.
     * The consensus algorithm should take actions required accordingly to
     * update the state.
     *
     * @param block Recently received block
     */
    @Override
    public abstract void newIncomingBlock(B block);

    /**
     * Check if the received block is valid according to the state of the chain.
     * This might include difficulty check or signature verification etc.
     *
     * @param block The block to check if it is valid or not
     * @return True if the block is valid according to the current state of the
     * chain
     */
    @Override
    public boolean isBlockValid(B block) { // TODO: This should check that the block meets required difficulty
        return true;
    } // for checking difficulty signature and etc

    /**
     * Check if this block is agreed by the consensus algorithm executed by node.
     *
     * @param block The block to check if it is agreed by consensus algorithm.
     * @return True if the block is accepted by the consensus algorithm.
     */
    @Override
    public boolean isBlockConfirmed(B block) {
        return confirmedBlocks.contains(block);
    }

    /**
     * Check if the provided transaction is inside a block that is agreed by the
     * node consensus algorithm.
     *
     * @param tx The transaction to check if it is included in a agreed block
     *           inside the consensus algorithm.
     * @return True if the transaction is inside a block that is agreed by the
     * consensus algorithm.
     */
    @Override
    public boolean isTxConfirmed(T tx) {
        return confirmedTxs.contains(tx);
    }

    /**
     * Returns the total number of blocks agreed by the consensus algorithm
     * executed by the node
     *
     * @return The total number of blocks agreed by consensus algorithm
     */
    @Override
    public int getNumOfConfirmedBlocks() {
        return confirmedBlocks.size();
    }

    /**
     * Returns the yotal number of accepted transactions by the consensus algorithm
     *
     * @return Total number of accepted transactions by the consensus algorithm
     */
    @Override
    public int getNumOfConfirmedTxs() {
        return confirmedTxs.size();
    }
}
