package jabs.consensus.algorithm;

import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.simulator.Simulator;
import jabs.simulator.event.BlockConfirmationEvent;
import jabs.simulator.event.BlockchainReorgEvent;

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
            if (!(localBlockTree.getAncestorOfHeight(block, this.longestChainLen)
                    .equals(this.currentMainChainHead))) {
                Simulator simulator = this.peerDLTNode.getSimulator();
                double currentTime = simulator.getCurrentTime();
                simulator.putEvent(
                        new BlockchainReorgEvent(currentTime, this.peerDLTNode, block,
                                block.getHeight() - this.longestChainLen),
                        0
                );
            }
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
            double currentTime = simulator.getCurrentTime();
            simulator.putEvent(
                    new BlockConfirmationEvent(currentTime, this.peerDLTNode, highestConfirmedBlock),
                    0);
        }
    }

    public double getAverageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }
}
