package main.java.data;

import main.java.data.Data;

public abstract class Hash extends Data {
    private final Data data;

    public Hash(int size, Data data) {
        super(size);
        this.data = data;
    }

    private Data getData() {
        return this.data;
    }  // should never be used
}
