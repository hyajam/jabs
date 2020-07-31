package main.java.message;

import main.java.data.Transaction;
import main.java.data.TxHash;
import main.java.node.nodes.Node;

import static main.java.network.BlockFactory.GET_DATA_OVERHEAD;

public class RequestTxMessage<T extends Transaction<T>> extends Message {
    private final TxHash<T> dataHash;

    public RequestTxMessage(Node from, Node to, TxHash<T> txHash) {
        super(txHash.getSize() + GET_DATA_OVERHEAD, from, to);
        this.dataHash = txHash;
    }

    public TxHash<T> getTxHash() {
        return this.dataHash;
    }
}
