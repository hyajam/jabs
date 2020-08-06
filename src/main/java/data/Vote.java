package main.java.data;

import main.java.node.nodes.Node;

public abstract class Vote extends Data {
    private final Node voter;

    protected Vote(int size, Node voter) {
        super(size);
        this.voter = voter;
    }

    public Node getVoter() {
        return voter;
    }
}
