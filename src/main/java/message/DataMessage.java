package main.java.message;

import main.java.data.Data;

public class DataMessage<D extends Data> extends Message {
    private final D data;

    public DataMessage(D data) {
        super(data.getSize(), MessageType.DATA);
        this.data = data;
    }

    public D getData() {
        return this.data;
    }
}
