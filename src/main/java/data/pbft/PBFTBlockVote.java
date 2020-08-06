package main.java.data.pbft;

import main.java.data.Block;
import main.java.data.Vote;
import main.java.node.nodes.Node;

public class PBFTBlockVote<B extends Block<B>> extends Vote {
    static final int VOTE_SIZE = 60;

    private final B block;
    private final VoteType voteType;
    public enum VoteType {
        PRE_PREPARE,
        PREPARE,
        COMMIT
    }

    protected PBFTBlockVote(Node voter, B block, VoteType voteType) {
        super(VOTE_SIZE, voter);
        this.block = block;
        this.voteType = voteType;
    }

    public VoteType getVoteType() {
        return this.voteType;
    }
    public B getBlock() {
        return this.block;
    }
}
