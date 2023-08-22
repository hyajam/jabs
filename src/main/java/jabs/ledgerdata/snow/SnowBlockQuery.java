package jabs.ledgerdata.snow;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Query;
import jabs.network.node.nodes.Node;
public class SnowBlockQuery <B extends Block<B>> extends Query {
    private final B block;
    private final SnowBlockQuery.QueryType queryType;

    public static final int SNOW_QUERY_SIZE_OVERHEAD = 10;

    public enum QueryType {
        PREPARE,
        COMMIT
    }

    protected SnowBlockQuery(int size, Node inquirer, B block, SnowBlockQuery.QueryType queryType) {
        super(size, inquirer);
        this.block = block;
        this.queryType = queryType;
    }

    public SnowBlockQuery.QueryType getQueryType() {
        return this.queryType;
    }
    public B getBlock() {
        return this.block;
    }
}
