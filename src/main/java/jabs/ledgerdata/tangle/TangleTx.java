package jabs.ledgerdata.tangle;

import jabs.ledgerdata.Tx;

public class TangleTx extends Tx<TangleTx> {
    public TangleTx(int size, int hashSize) {
        super(size, hashSize);
    }
}
