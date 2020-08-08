package main.java.message;

public abstract class Message {
    private final int size;
    private final MessageType messageType;

    public enum MessageType {
        DATA,
        INV,
        REQUEST_DATA,
        VOTE
    }

    public int getSize(){ return this.size; }
    public MessageType getMessageType() {
        return this.messageType;
    }

    public Message(int size, MessageType messageType) {
        this.size = size;
        this.messageType = messageType;
    }
}
