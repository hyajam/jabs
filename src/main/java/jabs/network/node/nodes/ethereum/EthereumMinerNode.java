package jabs.network.node.nodes.ethereum;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.config.GhostProtocolConfig;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.ledgerdata.ethereum.EthereumBlockWithTx;
import jabs.ledgerdata.ethereum.EthereumTx;
import jabs.network.message.DataMessage;
import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.MinerNode;
import jabs.network.node.nodes.Node;
import jabs.simulator.Simulator;
import jabs.simulator.event.BlockMiningProcess;

import java.util.HashSet;
import java.util.Set;

import static jabs.ledgerdata.BlockFactory.ETHEREUM_MIN_DIFFICULTY;

public class EthereumMinerNode extends EthereumNode implements MinerNode {
    protected Set<EthereumTx> memPool = new HashSet<>();
    protected Set<EthereumBlock> alreadyUncledBlocks = new HashSet<>();
    protected final double hashPower;
    protected Simulator.ScheduledEvent miningProcess;
    static final long MAXIMUM_BLOCK_GAS = 12500000;

    public EthereumMinerNode(Simulator simulator, Network network, int nodeID,
                             long downloadBandwidth, long uploadBandwidth, double hashPower, EthereumBlock genesisBlock,
                             GhostProtocolConfig ghostProtocolConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, genesisBlock, ghostProtocolConfig);
        this.hashPower = hashPower;
    }

    public EthereumMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                             long uploadBandwidth, double hashPower,
                             AbstractChainBasedConsensus<EthereumBlock, EthereumTx> consensusAlgorithm) {
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

        double weight = this.network.getRandom().sampleExponentialDistribution(1);
        EthereumBlockWithTx ethereumBlockWithTx = new EthereumBlockWithTx(
                canonicalChainHead.getHeight()+1, simulator.getCurrentTime(), this,
                this.getConsensusAlgorithm().getCanonicalChainHead(), tipBlocks, blockTxs, ETHEREUM_MIN_DIFFICULTY,
                weight); // TODO: Difficulty?

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(ethereumBlockWithTx)
                )
        );
    }

    /**
     *
     */
    @Override
    public void startMining() {
        BlockMiningProcess blockMiningProcess = new BlockMiningProcess(this.simulator, this.network.getRandom(),
                this.consensusAlgorithm.getCanonicalChainHead().getDifficulty()/((double) this.hashPower), this);
        this.miningProcess = this.simulator.putEvent(blockMiningProcess, blockMiningProcess.timeToNextGeneration());
    }

    /**
     *
     */
    @Override
    public void stopMining() {
        simulator.removeEvent(this.miningProcess);
    }

    public double getHashPower() {
        return this.hashPower;
    }

    @Override
    protected void processNewTx(EthereumTx ethereumTx, Node from) {
        // add to memPool
        memPool.add((EthereumTx) ethereumTx);

        this.broadcastTransaction((EthereumTx) ethereumTx, from);
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
