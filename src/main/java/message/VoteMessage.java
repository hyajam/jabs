package main.java.message;

import main.java.data.Vote;
import main.java.node.nodes.Node;

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
