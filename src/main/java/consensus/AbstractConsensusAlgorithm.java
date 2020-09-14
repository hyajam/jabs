package main.java.consensus;

import main.java.data.Block;
import main.java.data.Tx;

import java.util.HashSet;

public abstract class AbstractConsensusAlgorithm<B extends Block<B>, T extends Tx<T>>
        implements ConsensusAlgorithm<B, T> {
    protected HashSet<B> acceptedBlocks = new HashSet<>();
    protected final HashSet<T> acceptedTxs = new HashSet<>();

    @Override
    public abstract void newIncomingBlock(B block);

    @Override
    public boolean isBlockValid(B block) { // TODO: This should check that the block meets required difficulty
        return true;
    } // for checking difficulty signature and etc

    @Override
    public boolean isBlockAccepted(B block) {
        return acceptedBlocks.contains(block);
    }

    @Override
    public boolean isTxAccepted(T tx) {
        return acceptedTxs.contains(tx);
    }

    @Override
    public int getNumOfAcceptedBlocks() {
        return acceptedBlocks.size();
    }

    @Override
    public int getNumOfAcceptedTxs() {
        return acceptedTxs.size();
    }
}
