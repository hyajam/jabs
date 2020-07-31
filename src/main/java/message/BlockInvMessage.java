package main.java.message;

import main.java.data.Block;
import main.java.data.BlockHash;
import main.java.node.nodes.Node;

public class BlockInvMessage<B extends Block<B>> extends InvMessage<BlockHash<B>> {
    public BlockInvMessage(int size, Node from, Node to, BlockHash<B> hash) {
        super(size, from, to, hash);
    }

    public BlockHash<B> getBlockHash() {
        return this.getHash();
    }
}
