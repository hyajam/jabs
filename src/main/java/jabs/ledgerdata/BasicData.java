package jabs.ledgerdata;

public abstract class BasicData {
    protected int size;

    protected BasicData(int size) {
        this.size = size;
    }

    public int getSize() { return size; };
}
