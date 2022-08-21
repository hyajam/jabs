package jabs.ledgerdata.tangle;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.ProofOfWorkBlock;
import jabs.network.node.nodes.Node;

import java.util.List;

public class TangleBlock extends Block<TangleBlock> implements ProofOfWorkBlock {
    protected final TangleTx blockTx;
    private final double difficulty;
    private final double weight;
    public TangleBlock(int size, int height, double creationTime, Node creator, List<TangleBlock> parents,
                       int hashSize, TangleTx blockTx, double weight, double difficulty) {
        super(size, height, creationTime, creator, parents, hashSize);
        this.blockTx = blockTx;
        this.weight = weight;
        this.difficulty = difficulty;
    }

    /**
     * @return Difficulty of the block
     */
    @Override
    public double getDifficulty() {
        return this.difficulty;
    }

    /**
     * @return Weight of the block
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
}
