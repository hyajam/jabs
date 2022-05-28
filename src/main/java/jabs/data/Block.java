package jabs.data;

import jabs.node.nodes.Node;

public abstract class Block<B extends Block<B>> extends Data implements Comparable<Block<B>> {
    private final int height;
    private final double creationTime;
    private final B parent;
    private final Node creator;

    protected Block(int size, int height, double creationTime, Node creator, B parent, int hashSize) {
        super(size, hashSize);
        this.height = height;
        this.creationTime = creationTime;
        this.creator = creator;
        this.parent = parent;
    }

    public int getHeight() {
        return this.height;
    }

    public double getCreationTime() {
        return this.creationTime;
    }

    public Node getCreator() {
        return this.creator;
    }

    public B getParent() {
        return this.parent;
    }

    public int compareTo(Block<B> b) {
        return Integer.compare(this.height, b.getHeight());
    }
}
