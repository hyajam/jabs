package jabs.consensus.algorithm;

import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.simulator.Simulator;
import jabs.simulator.event.BlockConfirmationEvent;

public class NakamotoConsensus<B extends SingleParentBlock<B>, T extends Tx<T>>
        extends AbstractChainBasedConsensus<B, T> {
    private int longestChainLen = 0;
    private final double averageBlockMiningInterval;
    private final int confirmationDepth;

    public NakamotoConsensus(LocalBlockTree<B> localBlockTree, NakamotoConsensusConfig nakamotoConsensusConfig) {
        super(localBlockTree);
        this.averageBlockMiningInterval = nakamotoConsensusConfig.averageBlockMiningInterval();
        this.confirmationDepth = nakamotoConsensusConfig.getConfirmationDepth();
        this.currentMainChainHead = localBlockTree.getGenesisBlock();
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
        if (currentMainChainHead.getHeight() > confirmationDepth) {
            int heightOfConfirmedBlocks = currentMainChainHead.getHeight() - confirmationDepth;
            B highestConfirmedBlock =  localBlockTree.getAncestorOfHeight(currentMainChainHead, heightOfConfirmedBlocks);
            this.confirmedBlocks = this.localBlockTree.getAllAncestors(highestConfirmedBlock);
            Simulator simulator = this.peerDLTNode.getSimulator();
            double currentTime = simulator.getSimulationTime();
            simulator.putEvent(
                    new BlockConfirmationEvent(currentTime, this.peerDLTNode, highestConfirmedBlock),
                    0);
        }
    }

    public double getAverageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }
}
