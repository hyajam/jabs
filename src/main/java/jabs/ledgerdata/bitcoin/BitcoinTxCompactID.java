package jabs.ledgerdata.bitcoin;

import jabs.ledgerdata.TxCompactID;

public class BitcoinTxCompactID extends TxCompactID {
    public static final int BITCOIN_TX_COMPACT_ID_SIZE = 6;

    protected BitcoinTxCompactID() {
        super(BITCOIN_TX_COMPACT_ID_SIZE);
    }
}
