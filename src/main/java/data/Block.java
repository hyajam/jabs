package main.java.data;

import main.java.node.nodes.Node;

public abstract class Block<B extends Block<B>> extends Data {
    private final int height;
    private final long creationTime;
    private final B parent;
    private final Node creator;
    final BlockHash<B> blockHash;

    protected Block(int size, int height, long creationTime, Node creator, B parent, int hashSize) {
        super(size);
        this.height = height;
        this.creationTime = creationTime;
        this.creator = creator;
        this.parent = parent;
        this.blockHash = new BlockHash<>(hashSize, this);
    }

    public int getHeight() {
        return this.height;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public B getParent() {
        return (B) this.parent;
    }

    public BlockHash<B> getHash(){
        return  this.blockHash;
    }
}
