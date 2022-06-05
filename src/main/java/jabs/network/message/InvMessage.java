package jabs.network.message;

import jabs.ledgerdata.Hash;

import static jabs.network.networks.BlockFactory.INV_MESSAGE_OVERHEAD;

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
