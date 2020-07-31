package main.java.data;

public class PBFTVote<B extends Block<B>> extends Data {
    static final int VOTE_SIZE = 60;
    private final VoteType voteType;
    public enum VoteType {
        PREPARE,
        COMMIT
    }

    protected PBFTVote(B block, VoteType voteType) {
        super(VOTE_SIZE);
        this.voteType = voteType;
    }

    private VoteType getVoteType() {
        return this.voteType;
    }
}
