package main.java.data;

public abstract class Hash extends Data {
    private final Data data;

    public Hash(int size, Data data) {
        super(size);
        this.data = data;
    }

    private Data getBasicData() {
        return this.data;
    }  // should never be used
}
