package main.java.consensus;

import main.java.blockchain.LocalBlockTree;
import main.java.data.Block;
import main.java.data.Tx;

public class Xolph<B extends Block<B>, T extends Tx<T>> extends AbstractBlockchainConsensus<B, T>
        implements DeterministicFinalityConsensus<B, T> {

    public Xolph(LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
    }

    @Override
    protected void updateChain() {

    }

    @Override
    public void newIncomingBlock(B block) {

    }

    @Override
    public boolean isBlockFinalized(B block) {
        return false;
    }

    @Override
    public boolean isTxFinalized(T tx) {
        return false;
    }

    @Override
    public int getNumOfFinalizedBlocks() {
        return 0;
    }

    @Override
    public int getNumOfFinalizedTxs() {
        return 0;
    }
}
