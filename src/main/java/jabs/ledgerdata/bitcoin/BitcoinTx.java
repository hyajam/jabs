package jabs.ledgerdata.bitcoin;

import jabs.ledgerdata.Tx;

import static jabs.ledgerdata.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinTx extends Tx<BitcoinTx> {
    private final BitcoinTxCompactID compactID = new BitcoinTxCompactID();
    public BitcoinTx(int size) {
        super(size, BITCOIN_INV_SIZE);
    }
    public BitcoinTxCompactID getCompactID() {
        return this.compactID;
    }
}
