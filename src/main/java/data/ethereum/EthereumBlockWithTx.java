package main.java.data.ethereum;

import main.java.node.nodes.ethereum.EthereumMinerNode;

import static main.java.network.BlockFactory.ETHEREUM_BLOCK_HEADER_SIZE;
import static main.java.network.BlockFactory.ETHEREUM_BLOCK_HASH_SIZE;

import java.util.HashSet;
import java.util.Set;

public class EthereumBlockWithTx extends EthereumBlock {
    private final Set<EthereumTx> Txs;

    public EthereumBlockWithTx(int height, long creationTime, EthereumMinerNode creator, EthereumBlock parent, Set<EthereumBlock> uncles, Set<EthereumTx> txs, long difficulty) {
        super(0, height, creationTime, creator, parent, uncles, difficulty);
        Txs = txs;

        int totalSize = ETHEREUM_BLOCK_HEADER_SIZE;
        for (EthereumTx tx:txs) {
            totalSize += tx.getSize();
        }

        this.size = totalSize + (uncles.size() * ETHEREUM_BLOCK_HASH_SIZE);
    }

    public Set<EthereumTx> getTxs() { return new HashSet<>(this.Txs); }
}
