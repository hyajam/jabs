package main.java.consensus;

import main.java.blockchain.LocalBlockTree;
import main.java.data.Block;
import main.java.data.Tx;

import java.util.HashMap;
import java.util.HashSet;

public class GhostProtocol<B extends Block<B>, T extends Tx<T>> extends AbstractBlockchainConsensus<B, T> {
    private final HashMap<B, Integer> totalWeights = new HashMap<>();
    public static int DEFAULT_GHOST_WEIGHT = 1;
    protected B originOfGhost;

    public GhostProtocol(LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
        this.newIncomingBlock(localBlockTree.getGenesisBlock());
        this.originOfGhost = localBlockTree.getGenesisBlock();
    }

    @Override
    public void newIncomingBlock(B block) {
        totalWeights.put(block, DEFAULT_GHOST_WEIGHT);
        if (this.localBlockTree.getLocalBlock(block).isConnectedToGenesis) {
            for (B ancestor:this.localBlockTree.getAllAncestors(block)) {
                if (!totalWeights.containsKey(ancestor)) {
                    totalWeights.put(ancestor, DEFAULT_GHOST_WEIGHT);
                }
                totalWeights.put(ancestor, totalWeights.get(ancestor) + DEFAULT_GHOST_WEIGHT);
            }
        }
        B ghostMainChainHead = this.ghost();
        if (this.currentMainChainHead != ghostMainChainHead) {
            this.currentMainChainHead = ghostMainChainHead;
            updateChain();
        }
    }

    public B ghost() {
        B block;

        if (this.originOfGhost == null) {
            block = localBlockTree.getGenesisBlock();
        } else {
            block = this.originOfGhost;
        }


        while (true) {
            if (totalWeights.get(block) == 1) {
                return block;
            }

            int maxWeight = 0;
            HashSet<B> children = this.localBlockTree.getChildren(block);
            for (B child: children) {
                if (localBlockTree.getLocalBlock(child).isConnectedToGenesis) {
                    if (totalWeights.get(child) > maxWeight) {
                        maxWeight = totalWeights.get(child);
                        block = child;
                    }
                }
            }
        }
    }

    @Override
    protected void updateChain() {
        this.acceptedBlocks = this.localBlockTree.getAllAncestors(this.currentMainChainHead);
    }
}
