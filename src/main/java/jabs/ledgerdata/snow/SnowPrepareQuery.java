package jabs.ledgerdata.snow;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

public class SnowPrepareQuery <B extends Block<B>> extends SnowBlockQuery<B> {
    public SnowPrepareQuery(Node inquirer, B block) {
        super(block.getHash().getSize() + SNOW_QUERY_SIZE_OVERHEAD, inquirer, block, SnowBlockQuery.QueryType.PREPARE);
    }
}
