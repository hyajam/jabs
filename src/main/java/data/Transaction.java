package main.java.data;

public abstract class Transaction<T extends Transaction<T>> extends Data {
    final TxHash<T> txHash;

    protected Transaction(int size, int hashSize) {
        super(size);
        this.txHash = new TxHash<T>(hashSize, this);
    }

    public TxHash<T> getHash(){
        return  this.txHash;
    }
}
