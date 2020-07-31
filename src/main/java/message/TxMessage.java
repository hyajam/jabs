package main.java.message;

import main.java.node.nodes.Node;
import main.java.data.Transaction;

public class TxMessage<T extends Transaction<T>> extends Message {
    protected final T transaction;

    public TxMessage(int size, Node from, Node to, T transaction) {
        super(size, from, to);
        this.transaction = transaction;
    }

    public T getTransaction() {
        return this.transaction;
    }
}
