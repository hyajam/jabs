package main.java.message;

import main.java.data.Vote;
import main.java.node.nodes.Node;

public class VoteMessage<V extends Vote> extends Message {
    private final V vote;

    public VoteMessage(V vote) {
        super(vote.getSize(), MessageType.VOTE);
        this.vote = vote;
    }

    public V getVote() {
        return vote;
    }
}
