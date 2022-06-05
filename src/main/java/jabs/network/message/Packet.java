package jabs.network.message;

import jabs.network.node.nodes.Node;

public class Packet {
    private final int size;
    private final Node from;
    private final Node to;
    private final Message message;

    public int getSize(){ return this.size; }
    public Node getFrom(){ return this.from; }
    public Node getTo(){ return this.to; }
    public Message getMessage(){ return this.message; }

    public Packet(Node from, Node to, Message message) {
        this.size = message.getSize();
        this.from = from;
        this.to = to;
        this.message = message;
    }
}
