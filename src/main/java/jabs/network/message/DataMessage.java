package jabs.network.message;

import jabs.ledgerdata.Data;

public class DataMessage extends Message {
    private final Data data;

    public DataMessage(Data data) {
        super(data.getSize());
        this.data = data;
    }

    public Data getData() {
        return data;
    }
}
