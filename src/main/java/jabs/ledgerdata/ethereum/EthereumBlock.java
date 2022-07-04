package jabs.ledgerdata.ethereum;

import jabs.ledgerdata.SingleParentBlock;
import jabs.network.node.nodes.ethereum.EthereumMinerNode;

import java.util.Set;

import static jabs.ledgerdata.BlockFactory.ETHEREUM_BLOCK_HASH_SIZE;

public class EthereumBlock extends SingleParentBlock<EthereumBlock> {
    private final Set<EthereumBlock> uncles;
    private final double difficulty;

    public EthereumBlock(int size, int height, double creationTime, EthereumMinerNode creator, EthereumBlock parent,
                         Set<EthereumBlock> uncles, double difficulty, double weight) {
        super(size, height, creationTime, creator, parent, ETHEREUM_BLOCK_HASH_SIZE, weight);
        this.uncles = uncles;
        this.difficulty = difficulty;

        long unclesDifficultySum = 0;
        for (EthereumBlock uncle:uncles) {
            unclesDifficultySum += uncle.getDifficulty();
        }
    }

    public Set<EthereumBlock> getUncles() {
        return this.uncles;
    }

    public double getDifficulty() {
        return this.difficulty;
    }
}
