package jabs.ledgerdata.tangle;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

import java.util.List;

public class TangleBlock extends Block<TangleBlock> {
    protected final TangleTx blockTx;
    protected final int weight;
    public TangleBlock(int size, int height, double creationTime, Node creator, List<TangleBlock> parents,
                       int hashSize, TangleTx blockTx, int weight) {
        super(size, height, creationTime, creator, parents, hashSize);
        this.blockTx = blockTx;
        this.weight = weight;
    }

    public int getWeight() {
        return this.weight;
    }
}
