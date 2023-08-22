package jabs.ledgerdata.snow;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

public class SnowCommitQuery <B extends Block<B>> extends SnowBlockQuery<B> {
    public SnowCommitQuery(Node inquirer, B block) {
        super(block.getHash().getSize() + SNOW_QUERY_SIZE_OVERHEAD, inquirer, block, QueryType.COMMIT);
    }
}
