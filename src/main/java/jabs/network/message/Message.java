package jabs.network.message;

public abstract class Message {
    private final int size;

    public int getSize(){ return this.size; }

    public Message(int size) {
        this.size = size;
    }
}
