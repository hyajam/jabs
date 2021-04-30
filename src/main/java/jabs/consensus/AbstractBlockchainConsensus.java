package jabs.consensus;

import jabs.blockchain.LocalBlockTree;
import jabs.data.Block;
import jabs.data.Tx;
import jabs.node.nodes.BlockchainNode;

public abstract class AbstractBlockchainConsensus<B extends Block<B>, T extends Tx<T>> extends AbstractConsensusAlgorithm<B, T> {
    protected final LocalBlockTree<B> localBlockTree;
    protected BlockchainNode<B, T> blockchainNode;
    protected B currentMainChainHead = null;

    public AbstractBlockchainConsensus(LocalBlockTree<B> localBlockTree) {
        this.localBlockTree = localBlockTree;
    }

    public LocalBlockTree<B> getLocalBlockTree() {
        return this.localBlockTree;
    }

    public B getCanonicalChainHead() {
        return currentMainChainHead;
    }

    public void setNode(BlockchainNode<B, T> blockchainNode) {
        this.blockchainNode = blockchainNode;
    }

    abstract protected void updateChain();
}
