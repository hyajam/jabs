package jabs.ledgerdata.dagsper;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Vote;
import jabs.network.node.nodes.Node;

import java.util.HashSet;

public class DAGsperVote<B extends Block<B>> extends Vote {
    public static final int MIN_DAGSPER_VOTE_SIZE = 44;
    public final int latestFinalizedHeight;
    public final int toBeFinalizedHeight;
    public final B toBeJustified;
    public final HashSet<Integer> unaffectedHeights;

    public DAGsperVote(Node voter, int latestFinalizedHeight, int toBeFinalizedHeight, B toBeJustified,
                       HashSet<Integer> unaffectedHeights) {
        super(MIN_DAGSPER_VOTE_SIZE + 4*unaffectedHeights.size(), voter);
        this.latestFinalizedHeight = latestFinalizedHeight;
        this.toBeFinalizedHeight = toBeFinalizedHeight;
        this.toBeJustified = toBeJustified;
        this.unaffectedHeights = unaffectedHeights;
    }
}
