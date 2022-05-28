package jabs.node.nodes.bitcoin;

import jabs.consensus.AbstractBlockchainConsensus;
import jabs.data.bitcoin.BitcoinBlock;
import jabs.data.bitcoin.BitcoinTx;
import jabs.message.DataMessage;
import jabs.message.Packet;
import jabs.network.BlockFactory;
import jabs.network.Network;
import jabs.node.nodes.MinerNode;
import jabs.simulator.Simulator;


public class BitcoinMinerNodeWithoutTx extends BitcoinMinerNode implements MinerNode {
    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, long hashPower, AbstractBlockchainConsensus<BitcoinBlock, BitcoinTx> consensusAlgorithm) {
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
