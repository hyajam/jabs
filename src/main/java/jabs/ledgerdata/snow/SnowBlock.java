package jabs.ledgerdata.snow;

import jabs.ledgerdata.SingleParentBlock;
import jabs.network.node.nodes.Node;

public class SnowBlock extends SingleParentBlock<SnowBlock> {
    public static final int Snow_BLOCK_HASH_SIZE = 32;

    public SnowBlock(int size, int height, double creationTime, Node creator, SnowBlock parent) {
        super(size, height, creationTime, creator, parent, Snow_BLOCK_HASH_SIZE);
    }
}
