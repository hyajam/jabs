package main.java.message;

import main.java.data.Hash;
import main.java.node.nodes.Node;

import static main.java.network.BlockFactory.INV_MESSAGE_OVERHEAD;

public class InvMessage<H extends Hash> extends Message {
    private final H hash;

    public InvMessage(int size, H hash) {
        super(size + INV_MESSAGE_OVERHEAD, MessageType.INV);
        this.hash = hash;
    }

    public H getHash() { return this.hash; }
}
