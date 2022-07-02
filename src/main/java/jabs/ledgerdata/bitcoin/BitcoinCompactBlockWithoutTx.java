package jabs.ledgerdata.bitcoin;

import jabs.network.node.nodes.Node;

import java.util.HashSet;
import java.util.Set;

import static jabs.ledgerdata.BlockFactory.BITCOIN_BLOCK_HEADER_SIZE;

public class BitcoinCompactBlockWithoutTx extends BitcoinBlockWithoutTx {
    private final Set<BitcoinTxCompactID> TxCompactIDs = new HashSet<>();

    public BitcoinCompactBlockWithoutTx(int size, int height, double creationTime, Node creator, BitcoinBlockWithoutTx parent,
                                        Set<BitcoinTx> txs, double difficulty) {
        super(size, height, creationTime, creator, parent, difficulty);
    }

    // This is a very rough estimation of the size of a compact block in bitcoin
    public BitcoinCompactBlockWithoutTx(BitcoinBlockWithoutTx block) {
        super(0, block.getHeight(), block.getCreationTime(), block.getCreator(), block.getParent(),
                block.difficulty);

        this.size = (int)((((double)block.getSize() - BITCOIN_BLOCK_HEADER_SIZE) / 200) * 6) + BITCOIN_BLOCK_HEADER_SIZE;
    }

    public Set<BitcoinTxCompactID> getTxCompactIDs() {
        return TxCompactIDs;
    }
}
