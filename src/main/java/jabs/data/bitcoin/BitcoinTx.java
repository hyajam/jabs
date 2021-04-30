package jabs.data.bitcoin;

import jabs.data.Tx;

import static jabs.network.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinTx extends Tx<BitcoinTx> {
    public BitcoinTx(int size) {
        super(size, BITCOIN_INV_SIZE);
    }
}
