package jabs.ledgerdata.snow;

import jabs.ledgerdata.Tx;

public class SnowTx extends Tx<SnowTx> {
    protected SnowTx(int size, int hashSize) {
        super(size, hashSize);
    }
}
