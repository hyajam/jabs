package jabs.simulator.event;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

public class BlockConfirmationEvent extends AbstractLogEvent {
    /**
     * This is the node that confirms a block.
     */
    private final Node node;
    /**
     * The block that gets confirmed
     */
    private final Block block;
    public BlockConfirmationEvent(double time, Node node, Block block) {
        super(time);
        this.node = node;
        this.block = block;
    }

    public Node getNode() {
        return node;
    }

    public Block getBlock() {
        return block;
    }
}
