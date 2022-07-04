package jabs.ledgerdata.tangle;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

import java.util.List;

public class TangleBlock extends Block<TangleBlock> {
    protected final TangleTx blockTx;
    public TangleBlock(int size, int height, double creationTime, Node creator, List<TangleBlock> parents,
                       int hashSize, TangleTx blockTx, double weight) {
        super(size, height, creationTime, creator, parents, hashSize, weight);
        this.blockTx = blockTx;
    }
}
