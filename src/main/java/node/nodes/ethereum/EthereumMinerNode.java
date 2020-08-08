package main.java.node.nodes.ethereum;

import main.java.data.ethereum.EthereumBlock;
import main.java.data.ethereum.EthereumBlockWithTx;
import main.java.data.ethereum.EthereumTx;
import main.java.message.DataMessage;
import main.java.message.Packet;
import main.java.node.nodes.MinerNode;
import main.java.node.nodes.Node;
import main.java.simulator.AbstractSimulator;

import java.util.HashSet;
import java.util.Set;

import static main.java.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;

public class EthereumMinerNode extends EthereumNode implements MinerNode {
    protected Set<EthereumTx> memPool = new HashSet<>();
    protected Set<EthereumBlock> tipBlocks = new HashSet<>();
    protected final long hashPower;

    public EthereumMinerNode(int nodeID, int region, long hashPower) {
        super(nodeID, region);
        this.hashPower = hashPower;
    }

    public void generateNewBlock() {
        EthereumBlock canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();
        EthereumBlockWithTx ethereumBlockWithTx = new EthereumBlockWithTx(
                canonicalChainHead.getHeight()+1, AbstractSimulator.getCurrentTime(), this,
                this.getConsensusAlgorithm().getCanonicalChainHead(), tipBlocks, memPool, ETHEREUM_MIN_DIFFICULTY); // TODO: Difficulty?
        this.processIncomingMessage(
                new Packet(
                        this, this, new DataMessage<>(ethereumBlockWithTx)
                )
        );
        this.processNewBlock(ethereumBlockWithTx);
    }

    public long getHashPower() {
        return this.hashPower;
    }

    @Override
    protected void processNewTx(EthereumTx ethereumTx, Node from) {
        // add to memPool
        memPool.add(ethereumTx);

        this.broadcastTransaction(ethereumTx, from);
    }

    @Override
    protected void processNewBlock(EthereumBlock ethereumBlock) {
        this.consensusAlgorithm.newIncomingBlock(ethereumBlock);

        // remove from memPool
        if (ethereumBlock instanceof EthereumBlockWithTx) {
            for (EthereumTx ethereumTx: ((EthereumBlockWithTx) ethereumBlock).getTxs()) {
                memPool.remove(ethereumTx); // TODO: This should be changed. Ethereum reverts Txs from non canonical chain
            }
        }

        this.broadcastNewBlockAndBlockHashes(ethereumBlock);
    }
}
