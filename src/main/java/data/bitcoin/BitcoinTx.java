package main.java.data.bitcoin;

import main.java.data.Transaction;

import static main.java.network.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinTx extends Transaction<BitcoinTx> {
    public BitcoinTx(int size) {
        super(size, BITCOIN_INV_SIZE);
    }
}
