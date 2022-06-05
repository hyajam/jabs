package jabs.network.node.nodes.bitcoin;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.ledgerdata.bitcoin.BitcoinBlock;
import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.network.message.DataMessage;
import jabs.network.message.Packet;
import jabs.network.networks.BlockFactory;
import jabs.network.networks.Network;
import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;


public class BitcoinMinerNodeWithoutTx extends BitcoinMinerNode implements MinerNode {
    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, AbstractChainBasedConsensus<BitcoinBlock, BitcoinTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower, consensusAlgorithm);
    }

    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower);
    }

    @Override
    public void generateNewBlock() {
        BitcoinBlock canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();

        BitcoinBlock bitcoinBlock = BlockFactory.sampleBitcoinBlock(this.simulator,
                this.getNetwork().getRandom(), this, canonicalChainHead); // TODO: Difficulty?

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(bitcoinBlock)
                )
        );
    }

    @Override
    public long getHashPower() {
        return this.hashPower;
    }
}
