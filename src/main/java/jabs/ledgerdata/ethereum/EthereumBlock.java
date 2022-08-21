package jabs.ledgerdata.ethereum;

import jabs.ledgerdata.ProofOfWorkBlock;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.SingleParentPoWBlock;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.network.node.nodes.ethereum.EthereumMinerNode;

import java.util.HashSet;
import java.util.Set;

import static jabs.ledgerdata.BlockFactory.ETHEREUM_BLOCK_HASH_SIZE;

public class EthereumBlock extends SingleParentPoWBlock<EthereumBlock> implements ProofOfWorkBlock {
    private final Set<EthereumBlock> uncles;

    public EthereumBlock(int size, int height, double creationTime, EthereumMinerNode creator, EthereumBlock parent,
                         Set<EthereumBlock> uncles, double difficulty, double weight) {
        super(size, height, creationTime, creator, parent, ETHEREUM_BLOCK_HASH_SIZE, difficulty, weight);
        this.uncles = uncles;

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

    public static EthereumBlock generateGenesisBlock(double difficulty) {
        return new EthereumBlock(0, 0, 0, null, null, new HashSet<>(), difficulty, 0);
    }

    /**
     * @return 
     */
    @Override
    public double getWeight() {
        return this.weight;
    }
}
