package main.java.message;

import main.java.data.Data;
import main.java.data.Hash;

import static main.java.network.BlockFactory.INV_MESSAGE_OVERHEAD;

public class InvMessage extends Message {
    private final Hash hash;

    public InvMessage(int hashSize, Hash hash) {
        super(hashSize + INV_MESSAGE_OVERHEAD);
        this.hash = hash;
    }

    public Hash getHash() {
        return this.hash;
    }
}
