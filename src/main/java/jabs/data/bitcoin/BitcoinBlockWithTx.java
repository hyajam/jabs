package jabs.data.bitcoin;

import jabs.node.nodes.bitcoin.BitcoinMinerNode;

import java.util.HashSet;
import java.util.Set;

import static jabs.network.BlockFactory.BITCOIN_BLOCK_HEADER_SIZE;

public class BitcoinBlockWithTx extends BitcoinBlock {
    private final Set<BitcoinTx> Txs;

    public BitcoinBlockWithTx(int height, long creationTime, BitcoinBlock parent, BitcoinMinerNode creator, Set<BitcoinTx> txs) {
        super(0, height, creationTime, creator, parent);
        Txs = txs;

        int totalSize = BITCOIN_BLOCK_HEADER_SIZE;
        for (BitcoinTx tx:txs) {
            totalSize += tx.getSize();
        }

        this.size = totalSize;
    }

    public Set<BitcoinTx> getTxs() { return new HashSet<>(this.Txs); }
}
