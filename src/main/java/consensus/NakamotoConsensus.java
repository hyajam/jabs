package main.java.consensus;

import main.java.data.Block;
import main.java.data.Transaction;
import main.java.blockchain.LocalBlockTree;

public class NakamotoConsensus<B extends Block<B>, T extends Transaction<T>> extends AbstractBlockchainConsensus<B, T> {
    private int longestChainLen = -1;
    private B currentMainChainHead = null;

    public NakamotoConsensus(LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
        this.newBlock(localBlockTree.getGenesisBlock());
    }

    @Override
    public void newBlock(B block) {
        if (block.getHeight() > longestChainLen) {
            this.longestChainLen = block.getHeight();
            this.currentMainChainHead = block;
            this.acceptedBlocks = this.localBlockTree.getAllAncestors(block);
        }
    }

    public B getCanonicalChainHead() {
        return currentMainChainHead;
    }
}
