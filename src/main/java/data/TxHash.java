package main.java.data;

public class TxHash<T extends Transaction<T>> extends Hash {
    public TxHash(int size, Transaction<T> tx) {
        super(size, tx);
    }
}
