package main.java.consensus;

import main.java.data.Block;
import main.java.data.Transaction;
import main.java.blockchain.LocalBlockTree;

public class CasperFFG<B extends Block<B>, T extends Transaction<T>> extends AbstractBlockchainConsensus<B, T> {
    public CasperFFG(LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
    }

    @Override
    public void newBlock(Block block) {

    }

    @Override
    public B getCanonicalChainHead() {
        return null;
    }
}
