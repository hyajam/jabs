package jabs.consensus.algorithm;

import jabs.consensus.blockchain.LocalBlockDAG;
import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;
import jabs.network.node.nodes.PeerDLTNode;

/**
 * @param <B>
 * @param <T>
 */
public abstract class AbstractDAGBasedConsensus<B extends Block<B>, T extends Tx<T>>
        extends AbstractConsensusAlgorithm<B, T> {
    /**
     * Local Block Tree in Nodes Memory
     */
    protected final LocalBlockDAG<B> localBlockDAG;

    /**
     * The node that runs the consensus algorithm
     */
    protected PeerDLTNode<B, T> peerDLTNode;

    /**
     * Creates an Abstract DAG-based Consensus Algorithm
     * @param localBlockDAG local block tree in the node's memory
     */
    public AbstractDAGBasedConsensus(LocalBlockDAG<B> localBlockDAG) {
        this.localBlockDAG = localBlockDAG;
    }

    /**
     * Returns the local Block Tree that consensus algorithm uses
     * @return local block tree used by consensus
     */
    public LocalBlockDAG<B> getLocalBlockDAG() {
        return this.localBlockDAG;
    }

    /**
     * Sets the node using consensus algorithm (this cannot be set in Constructor function
     * since the algorithm is created during creation of the node, There might be better
     * ways to do it.)
     * @param peerDLTNode Sets the node using algorithm to input
     */
    public void setNode(PeerDLTNode<B, T> peerDLTNode) {
        this.peerDLTNode = peerDLTNode;
    }
}
