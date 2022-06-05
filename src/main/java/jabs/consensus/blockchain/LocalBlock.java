package jabs.consensus.blockchain;

import jabs.ledgerdata.Block;

import java.util.HashSet;

/**
 * LocalBlock is used for blocks that resides inside a nodes memory.
 * LocalBlocks have more information attached to them. Like whether
 * they are connected by other avalibale local blocks to genesis
 * or not.
 *
 * @param <B> any Block received by a node can be converted into a
 *           LocalBlock
 */
public class LocalBlock<B extends Block<B>> {
    /**
     * The block that is received by node
     */
    final public B block;

    /**
     * All children that the block has inside node local memory
     */
    final public HashSet<B> children;

    /**
     * Is the received block connected to genesis block by other
     * LocalBlocks available in nodes memory or not.
     */
    public boolean isConnectedToGenesis = false;

    /**
     * Creates a Local block by taking a normal received block
     * @param block a normal block or network block that is
     *              received by the node.
     */
    public LocalBlock(B block) {
        this.block = block;
        this.children = new HashSet<>();
    }
}
