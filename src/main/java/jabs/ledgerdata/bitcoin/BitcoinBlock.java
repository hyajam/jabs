package jabs.ledgerdata.bitcoin;

import jabs.ledgerdata.SingleParentBlock;
import jabs.network.node.nodes.Node;

import static jabs.network.networks.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinBlock extends SingleParentBlock<BitcoinBlock> {
    protected final double difficulty;
    public BitcoinBlock(int size, int height, double creationTime, Node creator, BitcoinBlock parent, double difficulty) {
        super(size, height, creationTime, creator, parent, BITCOIN_INV_SIZE);
        this.difficulty = difficulty;
    }

    public double getDifficulty(){
        return difficulty;
    }
}
