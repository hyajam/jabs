package main.java.data;

public abstract class Tx<T extends Tx<T>> extends Data {
    final TxHash<T> txHash;

    protected Tx(int size, int hashSize) {
        super(size);
        this.txHash = new TxHash<T>(hashSize, this);
    }

    public TxHash<T> getHash(){
        return  this.txHash;
    }
}
