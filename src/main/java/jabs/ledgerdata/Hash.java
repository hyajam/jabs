package jabs.ledgerdata;

public class Hash extends BasicData {
    private final Data data;

    public Hash(int size, Data data) {
        super(size);
        this.data = data;
    }

    public Data getData() {
        return this.data;
    }  // should never be used
}
