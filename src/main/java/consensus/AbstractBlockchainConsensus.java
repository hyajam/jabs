package main.java.consensus;

import main.java.data.Block;
import main.java.data.Transaction;
import main.java.blockchain.LocalBlockTree;

public abstract class AbstractBlockchainConsensus<B extends Block<B>, T extends Transaction<T>> extends AbstractConsensusAlgorithm<B, T> {
    protected final LocalBlockTree<B> localBlockTree;

    public AbstractBlockchainConsensus(LocalBlockTree<B> localBlockTree) {
        this.localBlockTree = localBlockTree;
    }

    public LocalBlockTree<B> getLocalBlockTree() {
        return this.localBlockTree;
    }

    public abstract B getCanonicalChainHead();
}
