package main.java.message;

import main.java.data.Hash;
import main.java.node.nodes.Node;

import static main.java.network.BlockFactory.GET_DATA_OVERHEAD;

public class RequestDataMessage extends Message {
    private final Hash payloadHash;

    public RequestDataMessage(Hash hash) {
        super(hash.getSize() + GET_DATA_OVERHEAD, MessageType.REQUEST_DATA);
        this.payloadHash = hash;
    }

    public Hash getHash() {
        return this.payloadHash;
    }
}
