package main.java.data.casper;

import main.java.data.Block;
import main.java.data.Vote;
import main.java.node.nodes.Node;

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
