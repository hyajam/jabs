package main.java.data.ethereum;

import main.java.data.Tx;

public class EthereumTx extends Tx<EthereumTx> {
    public EthereumTx(int size) {
        super(size, 0); // Ethereum does not use transaction hashes in network communication
    }
}
