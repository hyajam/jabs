package jabs.consensus;

import jabs.blockchain.LocalBlockTree;
import jabs.data.Block;
import jabs.data.Tx;
import jabs.node.nodes.BlockchainNode;

public abstract class AbstractBlockchainConsensus<B extends Block<B>, T extends Tx<T>> extends AbstractConsensusAlgorithm<B, T> {
    /**
     * Local Block Tree in Nodes Memory
     */
    protected final LocalBlockTree<B> localBlockTree;

    /**
     * The node that runs the consensus algorithm
     */
    protected BlockchainNode<B, T> blockchainNode;

    /**
     * The latest block agreed by consensus algorithm (Current agreed state by the Node)
     */
    protected B currentMainChainHead = null;

    /**
     * Creates a Abstract Blockchain Consensus Algorithm
     * @param localBlockTree local block tree in the node's memory
     */
    public AbstractBlockchainConsensus(LocalBlockTree<B> localBlockTree) {
        this.localBlockTree = localBlockTree;
    }

    /**
     * Returns the local Block Tree that consensus algorithm uses
     * @return local block tree used by consensus
     */
    public LocalBlockTree<B> getLocalBlockTree() {
        return this.localBlockTree;
    }

    /**
     * Returns the current head of the chain in blockchain consensus
     * @return block in the head of the chain
     */
    public B getCanonicalChainHead() {
        return currentMainChainHead;
    }

    /**
     * Sets the node using consensus algorithm (this cannot be set in Constructor function
     * since the algorithm is created during creation of the node, There might be better
     * ways to do it.)
     * @param blockchainNode Sets the node using algorithm to input
     */
    public void setNode(BlockchainNode<B, T> blockchainNode) {
        this.blockchainNode = blockchainNode;
    }

    abstract protected void updateChain();
}
