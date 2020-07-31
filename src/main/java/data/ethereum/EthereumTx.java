package main.java.data.ethereum;

import main.java.data.Transaction;

public class EthereumTx extends Transaction<EthereumTx> {
    public EthereumTx(int size) {
        super(size, 0); // Ethereum does not use transaction hashes in network communication
    }
}
