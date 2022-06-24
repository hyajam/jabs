package jabs.ledgerdata.bitcoin;

import jabs.ledgerdata.Tx;

import static jabs.ledgerdata.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinTx extends Tx<BitcoinTx> {
    public BitcoinTx(int size) {
        super(size, BITCOIN_INV_SIZE);
    }
}
