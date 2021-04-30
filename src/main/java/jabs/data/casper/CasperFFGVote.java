package jabs.data.casper;

import jabs.data.Block;
import jabs.data.Vote;
import jabs.node.nodes.Node;

public class CasperFFGVote<B extends Block<B>> extends Vote {
    private final CasperFFGLink<B> link;
    public static final int CASPER_VOTE_SIZE = 72;

    public CasperFFGVote(Node voter, CasperFFGLink<B> casperFFGLink) {
        super(CASPER_VOTE_SIZE, voter);
        this.link = casperFFGLink;
    }

    public final CasperFFGLink<B> getLink() {
        return this.link;
    }
}
