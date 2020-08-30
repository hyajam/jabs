package main.java.data.dagsper;

import main.java.data.Block;
import main.java.data.Vote;
import main.java.node.nodes.Node;

import java.util.HashSet;

public class DAGsperVote<B extends Block<B>> extends Vote {
    public static final int DAGSPER_VOTE_SIZE = 100;
    public final B latestFinalized;
    public final B toBeFinalized;
    public final B toBeJustified;
    public final HashSet<Integer> unaffectedHeights;

    public DAGsperVote(Node voter, B latestFinalized, B toBeFinalized, B toBeJustified,
                       HashSet<Integer> unaffectedHeights) {
        super(DAGSPER_VOTE_SIZE, voter);
        this.latestFinalized = latestFinalized;
        this.toBeFinalized = toBeFinalized;
        this.toBeJustified = toBeJustified;
        this.unaffectedHeights = unaffectedHeights;
    }
}
