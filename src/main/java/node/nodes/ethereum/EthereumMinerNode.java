package main.java.node.nodes.ethereum;

import main.java.consensus.AbstractBlockchainConsensus;
import main.java.data.ethereum.EthereumBlock;
import main.java.data.ethereum.EthereumBlockWithTx;
import main.java.data.ethereum.EthereumTx;
import main.java.message.DataMessage;
import main.java.message.Packet;
import main.java.node.nodes.MinerNode;
import main.java.node.nodes.Node;
import main.java.simulator.Simulator;

import java.util.HashSet;
import java.util.Set;

import static main.java.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;

public class EthereumMinerNode extends EthereumNode implements MinerNode {
    protected Set<EthereumTx> memPool = new HashSet<>();
    protected Set<EthereumBlock> alreadyUncledBlocks = new HashSet<>();
    protected final long hashPower;
    static final long MAXIMUM_BLOCK_GAS = 12500000;

    public EthereumMinerNode(int nodeID, int region, long hashPower) {
        super(nodeID, region);
        this.hashPower = hashPower;
    }

    public EthereumMinerNode(int nodeID, int region, long hashPower,
                             AbstractBlockchainConsensus<EthereumBlock, EthereumTx> consensusAlgorithm) {
        super(nodeID, region, consensusAlgorithm);
        this.hashPower = hashPower;
    }

    public void generateNewBlock() {
        EthereumBlock canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();

        Set<EthereumBlock> tipBlocks = this.localBlockTree.getChildlessBlocks();
        tipBlocks.remove(canonicalChainHead);
        tipBlocks.removeAll(alreadyUncledBlocks);

        Set<EthereumTx> blockTxs = new HashSet<>();
        long totalGas = 0;
        for (EthereumTx ethereumTx:memPool) {
            if ((totalGas + ethereumTx.getGas()) > MAXIMUM_BLOCK_GAS) {
                break;
            }
            blockTxs.add(ethereumTx);
            totalGas += ethereumTx.getGas();
        }

        EthereumBlockWithTx ethereumBlockWithTx = new EthereumBlockWithTx(
                canonicalChainHead.getHeight()+1, Simulator.getCurrentTime(), this,
                this.getConsensusAlgorithm().getCanonicalChainHead(), tipBlocks, blockTxs, ETHEREUM_MIN_DIFFICULTY); // TODO: Difficulty?

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(ethereumBlockWithTx)
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

        alreadyUncledBlocks.addAll(ethereumBlock.getUncles());

        // remove from memPool
        if (ethereumBlock instanceof EthereumBlockWithTx) {
            for (EthereumTx ethereumTx: ((EthereumBlockWithTx) ethereumBlock).getTxs()) {
                memPool.remove(ethereumTx); // TODO: This should be changed. Ethereum reverts Txs from non canonical chain
            }
        }

        this.broadcastNewBlockAndBlockHashes(ethereumBlock);
    }
}
