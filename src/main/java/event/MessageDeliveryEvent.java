package main.java.event;

import main.java.message.Message;

public class MessageDeliveryEvent implements Event {
    private final Message message;

    public MessageDeliveryEvent(Message message) {
        this.message = message;
    }

    public void execute(){
        this.message.getTo().getNodeNetworkInterface().addToDownLinkQueue(this.message);
    }
}
