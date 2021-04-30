package jabs.message;

import jabs.data.Vote;

public class VoteMessage extends Message {
    private final Vote vote;

    public VoteMessage(Vote vote) {
        super(vote.getSize());
        this.vote = vote;
    }

    public Vote getVote() {
        return vote;
    }
}
