package main.java.message;

import main.java.data.Block;
import main.java.node.nodes.Node;

public class BlockMessage<B extends Block<B>> extends Message {
    private final Block<B> block;

    public BlockMessage(int size, Node from, Node to, Block<B> block) {
        super(size, from, to);
        this.block = block;
    }

    public Block<B> getBlock() {
        return this.block;
    }
}
