package main.java.consensus;

import main.java.data.Block;
import main.java.data.Tx;
import main.java.blockchain.LocalBlockTree;

public class CasperFFG<B extends Block<B>, T extends Tx<T>> extends AbstractBlockchainConsensus<B, T> {
    public CasperFFG(LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
    }

    @Override
    public void newIncomingBlock(B block) {

    }

    @Override
    public B getCanonicalChainHead() {
        return null;
    }
}
