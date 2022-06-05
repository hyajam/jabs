package jabs.ledgerdata;

public abstract class Data extends BasicData {
    final Hash hash;
    public enum DataType {
        BLOCK,
        TX,
    }

    protected Data(int size, int hashSize) {
        super(size);
        this.hash = new Hash(hashSize, this);
    }

    public Hash getHash() {
        return hash;
    }
}
