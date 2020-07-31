package main.java.message;

import main.java.data.Block;
import main.java.data.BlockHash;
import main.java.node.nodes.Node;

import static main.java.network.BlockFactory.GET_DATA_OVERHEAD;

public class RequestBlockMessage<B extends Block<B>> extends Message {
    private final BlockHash<B> dataHash;

    public RequestBlockMessage(Node from, Node to, BlockHash<B> blockHash) {
        super(blockHash.getSize() + GET_DATA_OVERHEAD, from, to);
        this.dataHash = blockHash;
    }

    public BlockHash<B> getBlockHash() {
        return this.dataHash;
    }
}
