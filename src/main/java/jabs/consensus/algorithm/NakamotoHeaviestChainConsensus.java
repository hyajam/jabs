package jabs.consensus.algorithm;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.SingleParentPoWBlock;
import jabs.ledgerdata.Tx;
import jabs.simulator.Simulator;

import java.util.HashSet;

public class NakamotoHeaviestChainConsensus<B extends SingleParentPoWBlock<B>, T extends Tx<T>>
        extends AbstractChainBasedConsensus<B, T> {
    private double heaviestChainWeight = 0;
    private final double averageBlockMiningInterval;
    private final int confirmationDepth;

    public NakamotoHeaviestChainConsensus(LocalBlockTree<B> localBlockTree, NakamotoConsensusConfig nakamotoConsensusConfig) {
        super(localBlockTree);
        this.averageBlockMiningInterval = nakamotoConsensusConfig.averageBlockMiningInterval();
        this.confirmationDepth = nakamotoConsensusConfig.getConfirmationDepth();
        this.currentMainChainHead = localBlockTree.getGenesisBlock();
    }

    @Override
    public void newIncomingBlock(B block) {
        HashSet<B> pathToGenesis = localBlockTree.getAllAncestors(block);
        double totalWeight = block.getWeight();
        for (B ancestorBlock:pathToGenesis) {
            totalWeight += ancestorBlock.getWeight();
        }
        if (totalWeight > heaviestChainWeight) {
            this.heaviestChainWeight = totalWeight;
            this.currentMainChainHead = block;
            this.updateChain();
        }
    }

    @Override
    protected void updateChain() {

    }

    public double getAverageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }
}
