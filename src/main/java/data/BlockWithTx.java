package main.java.data;

import java.util.Set;

public interface BlockWithTx<T extends Tx<T>> {
    Set<T> getTxs();
}
