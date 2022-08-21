package jabs.ledgerdata;

import jabs.network.node.nodes.Node;

import java.util.ArrayList;

public class SingleParentPoWBlock<B extends SingleParentPoWBlock<B>> extends SingleParentBlock<B> implements ProofOfWorkBlock{
    protected final double difficulty;
    protected final double weight;

    public SingleParentPoWBlock(int size, int height, double creationTime, Node creator, B parent, int hashSize, double difficulty, double weight) {
        super(size, height, creationTime, creator, parent, hashSize);
        this.difficulty = difficulty;
        this.weight = weight;
    }

    /**
     * @return 
     */
    @Override
    public double getDifficulty() {
        return this.difficulty;
    }

    /**
     * @return 
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
}
