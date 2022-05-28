package jabs.node.nodes.bitcoin;

import jabs.consensus.AbstractBlockchainConsensus;
import jabs.data.bitcoin.BitcoinBlock;
import jabs.data.bitcoin.BitcoinBlockWithTx;
import jabs.data.bitcoin.BitcoinTx;
import jabs.message.DataMessage;
import jabs.message.Packet;
import jabs.network.Network;
import jabs.node.nodes.MinerNode;
import jabs.simulator.Simulator;

import java.util.HashSet;
import java.util.Set;

import static jabs.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;

public class BitcoinMinerNode extends BitcoinNode implements MinerNode {
    protected Set<BitcoinTx> memPool = new HashSet<>();
    protected final long hashPower;

    static final long MAXIMUM_BLOCK_SIZE = 1800000;

    public BitcoinMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, AbstractBlockchainConsensus<BitcoinBlock, BitcoinTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, consensusAlgorithm);
        this.hashPower = hashPower;
    }

    public BitcoinMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth);
        this.hashPower = hashPower;
    }


    @Override
    public void generateNewBlock() {
        BitcoinBlock canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();

        Set<BitcoinTx> blockTxs = new HashSet<>();
        long totalTxSize = 0;
        for (BitcoinTx bitcoinTx:memPool) {
            if ((totalTxSize + bitcoinTx.getSize()) > MAXIMUM_BLOCK_SIZE) {
                break;
            }
            blockTxs.add(bitcoinTx);
            totalTxSize += bitcoinTx.getSize();
        }

        BitcoinBlockWithTx bitcoinBlockWithTx = new BitcoinBlockWithTx(
                canonicalChainHead.getHeight()+1, simulator.getCurrentTime(),
                this.getConsensusAlgorithm().getCanonicalChainHead(), this, blockTxs); // TODO: Difficulty?

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(bitcoinBlockWithTx)
                )
        );
    }

    @Override
    public long getHashPower() {
        return this.hashPower;
    }
}
