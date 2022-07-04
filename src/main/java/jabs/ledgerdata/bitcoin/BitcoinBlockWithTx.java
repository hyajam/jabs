package jabs.ledgerdata.bitcoin;

import jabs.network.node.nodes.MinerNode;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.bitcoin.BitcoinNode;

import java.util.HashSet;
import java.util.Set;

import static jabs.ledgerdata.BlockFactory.BITCOIN_BLOCK_HEADER_SIZE;

public class BitcoinBlockWithTx extends BitcoinBlockWithoutTx {
    private final Set<BitcoinTx> Txs;

    public BitcoinBlockWithTx(int height, double creationTime, BitcoinBlockWithoutTx parent, Node creator,
                              Set<BitcoinTx> txs, double difficulty, double weight) {
        super(0, height, creationTime, creator, parent, difficulty, weight);
        this.Txs = txs;

        int totalSize = BITCOIN_BLOCK_HEADER_SIZE;
        for (BitcoinTx tx:txs) {
            totalSize += tx.getSize();
        }

        this.size = totalSize;
    }

    public Set<BitcoinTx> getTxs() { return new HashSet<>(this.Txs); }
}
