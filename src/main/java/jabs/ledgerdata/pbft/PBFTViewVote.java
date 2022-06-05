package jabs.ledgerdata.pbft;

import jabs.ledgerdata.Vote;
import jabs.network.node.nodes.Node;

public abstract class PBFTViewVote extends Vote {
    static final int VOTE_SIZE = 60;
    private final VoteType voteType;

    public enum VoteType {
        VIEW_CHANGE,
        NEW_VIEW
    }

    protected PBFTViewVote(Node voter, VoteType voteType) {
        super(VOTE_SIZE, voter);
        this.voteType = voteType;
    }
}
