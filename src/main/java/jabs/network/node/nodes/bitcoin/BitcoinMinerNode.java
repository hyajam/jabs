package jabs.network.node.nodes.bitcoin;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.ledgerdata.bitcoin.BitcoinBlock;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithTx;
import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.network.message.DataMessage;
import jabs.network.message.Packet;
import jabs.network.networks.Network;
import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;

import java.util.HashSet;
import java.util.Set;

public class BitcoinMinerNode extends BitcoinNode implements MinerNode {
    protected Set<BitcoinTx> memPool = new HashSet<>();
    protected final long hashPower;

    static final long MAXIMUM_BLOCK_SIZE = 1800000;

    public BitcoinMinerNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, AbstractChainBasedConsensus<BitcoinBlock, BitcoinTx> consensusAlgorithm) {
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
