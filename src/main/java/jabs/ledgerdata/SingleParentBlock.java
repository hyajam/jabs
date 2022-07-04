package jabs.ledgerdata;

import jabs.network.node.nodes.Node;

import java.util.ArrayList;

public class SingleParentBlock<B extends SingleParentBlock<B>> extends Block<B> {
    protected SingleParentBlock(int size, int height, double creationTime, Node creator, B parent, int hashSize,
                                double weight) {
        super(size, height, creationTime, creator, new ArrayList<>(), hashSize, weight);
        this.getParents().add(parent);
    }

    public <B extends SingleParentBlock<B>> B getParent() {
        return (B) this.getParents().get(0);
    }
}
