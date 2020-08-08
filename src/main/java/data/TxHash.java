package main.java.data;

public class TxHash<T extends Tx<T>> extends Hash {
    public TxHash(int size, Tx<T> tx) {
        super(size, tx);
    }
}
