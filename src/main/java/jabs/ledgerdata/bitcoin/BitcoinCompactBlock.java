package jabs.ledgerdata.bitcoin;

import jabs.network.node.nodes.Node;

import java.util.HashSet;
import java.util.Set;

import static jabs.ledgerdata.BlockFactory.BITCOIN_BLOCK_HEADER_SIZE;

public class BitcoinCompactBlock extends BitcoinBlockWithoutTx {
    private final Set<BitcoinTxCompactID> TxCompactIDs = new HashSet<>();

    public BitcoinCompactBlock(int height, double creationTime, Node creator, BitcoinBlockWithoutTx parent,
                               Set<BitcoinTx> txs, double difficulty, double weight) {
        super(0, height, creationTime, creator, parent, difficulty, weight);

        int totalSize = BITCOIN_BLOCK_HEADER_SIZE;
        for (BitcoinTx tx:txs) {
            TxCompactIDs.add(tx.getCompactID());
            totalSize += tx.getSize();
        }

        this.size = totalSize;
    }

    public BitcoinCompactBlock(BitcoinBlockWithTx block) {
        super(0, block.getHeight(), block.getCreationTime(), block.getCreator(), block.getParent(),
                block.difficulty, block.getWeight());

        int totalSize = BITCOIN_BLOCK_HEADER_SIZE;

        for (BitcoinTx tx: block.getTxs()) {
            TxCompactIDs.add(tx.getCompactID());
            totalSize += tx.getSize();
        }

        this.size = totalSize;
    }

    public Set<BitcoinTxCompactID> getTxCompactIDs() {
        return TxCompactIDs;
    }
}
