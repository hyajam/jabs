package jabs.consensus.algorithm;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.network.node.nodes.PeerBlockchainNode;

/**
 * @param <B>
 * @param <T>
 */
public abstract class AbstractChainBasedConsensus<B extends SingleParentBlock<B>, T extends Tx<T>>
        extends AbstractDAGBasedConsensus<B, T> {
    /**
     * Local Block Tree in Nodes Memory
     */
    protected final LocalBlockTree<B> localBlockTree;

    /**
     * The node that runs the consensus algorithm
     */
    protected PeerBlockchainNode<B, T> peerBlockchainNode;

    /**
     * The latest block agreed by consensus algorithm (Current agreed state by the Node)
     */
    protected B currentMainChainHead;

    /**
     * Creates a Abstract Blockchain Consensus Algorithm
     * @param LocalBlockTree local block tree in the node's memory
     */
    public AbstractChainBasedConsensus(LocalBlockTree<B> LocalBlockTree) {
        super(LocalBlockTree);
        this.localBlockTree = LocalBlockTree;
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
     * @param peerBlockchainNode Sets the node using algorithm to input
     */
    public void setNode(PeerBlockchainNode<B, T> peerBlockchainNode) {
        this.peerBlockchainNode = peerBlockchainNode;
    }

    abstract protected void updateChain();
}
