package jabs.consensus.algorithm;

import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.consensus.blockchain.LocalBlockTree;

public class NakamotoConsensus<B extends SingleParentBlock<B>, T extends Tx<T>> extends AbstractChainBasedConsensus<B, T> {
    private int longestChainLen = -1;

    public NakamotoConsensus(LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
        this.newIncomingBlock(localBlockTree.getGenesisBlock());
    }

    @Override
    public void newIncomingBlock(B block) {
        if (block.getHeight() > longestChainLen) {
            this.longestChainLen = block.getHeight();
            this.currentMainChainHead = block;
            this.updateChain();
        }
    }

    @Override
    protected void updateChain() {
        this.acceptedBlocks = this.localBlockTree.getAllAncestors(this.currentMainChainHead);
    }
}
