package jabs.data.pbft;

import jabs.data.Tx;

public class PBFTTx extends Tx<PBFTTx> {
    protected PBFTTx(int size, int hashSize) {
        super(size, hashSize);
    }
}
