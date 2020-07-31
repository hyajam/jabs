package main.java.message;

import main.java.node.nodes.Node;

public abstract class Message {
    private final int size;
    private final Node from;
    private final Node to;

    public int getSize(){ return this.size; }
    public Node getFrom(){ return this.from; }
    public Node getTo(){ return this.to; }

    public Message(int size, Node from, Node to) {
        this.size = size;
        this.from = from;
        this.to = to;
    }
}
