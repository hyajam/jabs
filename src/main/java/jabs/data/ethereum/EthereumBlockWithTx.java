package jabs.data.ethereum;

import jabs.data.BlockWithTx;
import jabs.node.nodes.ethereum.EthereumMinerNode;

import static jabs.network.BlockFactory.ETHEREUM_BLOCK_HEADER_SIZE;
import static jabs.network.BlockFactory.ETHEREUM_BLOCK_HASH_SIZE;

import java.util.HashSet;
import java.util.Set;

public class EthereumBlockWithTx extends EthereumBlock implements BlockWithTx<EthereumTx> {
    private final Set<EthereumTx> Txs;
    private final long totalGas;

    public EthereumBlockWithTx(int height, long creationTime, EthereumMinerNode creator, EthereumBlock parent, Set<EthereumBlock> uncles, Set<EthereumTx> txs, long difficulty) {
        super(0, height, creationTime, creator, parent, uncles, difficulty);
        Txs = txs;

        int totalSize = ETHEREUM_BLOCK_HEADER_SIZE;
        for (EthereumTx tx:txs) {
            totalSize += tx.getSize();
        }

        this.size = totalSize + (uncles.size() * ETHEREUM_BLOCK_HASH_SIZE);

        long totalGasTemp = 0;
        for (EthereumTx tx:txs) {
            totalGasTemp += tx.getGas();
        }

        totalGas = totalGasTemp;
    }

    @Override
    public Set<EthereumTx> getTxs() { return new HashSet<>(this.Txs); }

    public long getTotalGas() {
        return totalGas;
    }
}
