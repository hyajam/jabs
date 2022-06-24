package jabs.consensus.algorithm;

import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.consensus.blockchain.LocalBlockTree;

public class NakamotoConsensus<B extends SingleParentBlock<B>, T extends Tx<T>>
        extends AbstractChainBasedConsensus<B, T> {
    private int longestChainLen = -1;
    private final double averageBlockMiningInterval;

    public NakamotoConsensus(LocalBlockTree<B> localBlockTree, NakamotoConsensusConfig nakamotoConsensusConfig) {
        super(localBlockTree);
        this.averageBlockMiningInterval = nakamotoConsensusConfig.averageBlockMiningInterval();
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

    public double getAverageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }
}
