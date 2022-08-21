package jabs.ledgerdata.bitcoin;

import jabs.ledgerdata.SingleParentPoWBlock;
import jabs.network.node.nodes.Node;

import static jabs.ledgerdata.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinBlockWithoutTx extends SingleParentPoWBlock<BitcoinBlockWithoutTx> {

    public BitcoinBlockWithoutTx(int size, int height, double creationTime, Node creator, BitcoinBlockWithoutTx parent,
                                 double difficulty, double weight) {
        super(size, height, creationTime, creator, parent, BITCOIN_INV_SIZE, difficulty, weight);;
    }

    public double getDifficulty(){
        return difficulty;
    }

    /**
     * @return 
     */
    @Override
    public double getWeight() {
        return this.weight;
    }

    public static BitcoinBlockWithoutTx generateGenesisBlock(double difficulty) {
        return new BitcoinBlockWithoutTx(0, 0, 0, null, null, difficulty, 0);
    }
}
