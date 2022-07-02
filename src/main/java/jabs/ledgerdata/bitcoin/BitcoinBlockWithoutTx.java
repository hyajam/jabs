package jabs.ledgerdata.bitcoin;

import jabs.ledgerdata.SingleParentBlock;
import jabs.network.node.nodes.Node;

import static jabs.ledgerdata.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinBlockWithoutTx extends SingleParentBlock<BitcoinBlockWithoutTx> {
    protected final double difficulty;
    public BitcoinBlockWithoutTx(int size, int height, double creationTime, Node creator, BitcoinBlockWithoutTx parent, double difficulty) {
        super(size, height, creationTime, creator, parent, BITCOIN_INV_SIZE);
        this.difficulty = difficulty;
    }

    public double getDifficulty(){
        return difficulty;
    }
}
