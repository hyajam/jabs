package main.java.message;

import main.java.data.Hash;
import main.java.node.nodes.Node;

import static main.java.network.BlockFactory.INV_MESSAGE_OVERHEAD;

public abstract class InvMessage<H extends Hash> extends Message {
    private final H hash;

    public InvMessage(int size, Node from, Node to, H hash) {
        super(size + INV_MESSAGE_OVERHEAD, from, to);
        this.hash = hash;
    }

    public H getHash() { return this.hash; }
}
