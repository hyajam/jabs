package main.java.message;

import main.java.data.Transaction;
import main.java.data.TxHash;
import main.java.node.nodes.Node;

public class TxInvMessage<T extends Transaction<T>> extends InvMessage<TxHash<T>> {
    public TxInvMessage(int size, Node from, Node to, TxHash<T> hash) {
        super(size, from, to, hash);
    }

    public TxHash<T> getTxHash() {
        return this.getHash();
    }
}
