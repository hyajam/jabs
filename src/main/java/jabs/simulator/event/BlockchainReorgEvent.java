package jabs.simulator.event;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

public class BlockchainReorgEvent extends AbstractLogEvent {
    /**
     * This is the node that confirms a block.
     */
    private final Node node;
    /**
     * The block that gets confirmed
     */
    private final Block block;
    /**
     * The length of the reorganization in blockchain. In other words the number of consecutive blocks that gets
     * orphaned
     */
    private final int reorgLength;
    public BlockchainReorgEvent(double time, Node node, Block block, int reorgLength) {
        super(time);
        this.node = node;
        this.block = block;
        this.reorgLength = reorgLength;
    }

    public Node getNode() {
        return node;
    }

    public Block getBlock() {
        return block;
    }

    public int getReorgLength() {
        return reorgLength;
    }
}
