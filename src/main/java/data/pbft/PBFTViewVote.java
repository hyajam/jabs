package main.java.data.pbft;

import main.java.data.Vote;
import main.java.node.nodes.Node;

public class PBFTViewVote extends Vote {
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
