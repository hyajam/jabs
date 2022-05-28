package jabs.node.nodes.ethereum;

import jabs.consensus.AbstractBlockchainConsensus;
import jabs.data.ethereum.EthereumBlock;
import jabs.data.ethereum.EthereumBlockWithTx;
import jabs.data.ethereum.EthereumTx;
import jabs.message.DataMessage;
import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.nodes.MinerNode;
import jabs.node.nodes.Node;
import jabs.simulator.Simulator;

import java.util.HashSet;
import java.util.Set;

import static jabs.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;

public class EthereumMinerNode extends EthereumNode implements MinerNode {
    protected Set<EthereumTx> memPool = new HashSet<>();
    protected Set<EthereumBlock> alreadyUncledBlocks = new HashSet<>();
    protected final long hashPower;
    static final long MAXIMUM_BLOCK_GAS = 12500000;

    public EthereumMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth);
        this.hashPower = hashPower;
    }

    public EthereumMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower,
                             AbstractBlockchainConsensus<EthereumBlock, EthereumTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, consensusAlgorithm);
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
                canonicalChainHead.getHeight()+1, simulator.getCurrentTime(), this,
                this.getConsensusAlgorithm().getCanonicalChainHead(), tipBlocks, blockTxs, ETHEREUM_MIN_DIFFICULTY); // TODO: Difficulty?

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(ethereumBlockWithTx)
                )
        );
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
