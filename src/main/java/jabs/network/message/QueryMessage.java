package jabs.network.message;

import jabs.ledgerdata.Query;

public class QueryMessage extends Message{
    private final Query query;

    public QueryMessage(Query query) {
        super(query.getSize());
        this.query = query;
    }

    public Query getQuery() {
        return query;
    }
}
