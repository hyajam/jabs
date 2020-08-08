package main.java.data.pbft;

import main.java.data.Tx;

public class PBFTTx extends Tx<PBFTTx> {
    protected PBFTTx(int size, int hashSize) {
        super(size, hashSize);
    }
}
