package jabs.network.node.nodes.bitcoin;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.bitcoin.BitcoinBlockWithoutTx;
import jabs.ledgerdata.bitcoin.BitcoinCompactBlockWithoutTx;
import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.network.message.DataMessage;
import jabs.network.message.Packet;
import jabs.ledgerdata.BlockFactory;
import jabs.network.networks.Network;
import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;


public class BitcoinMinerNodeWithoutTx extends BitcoinMinerNode implements MinerNode {
    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                                     long uploadBandwidth, double hashPower,
                                     AbstractChainBasedConsensus<BitcoinBlockWithoutTx, BitcoinTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower,
                consensusAlgorithm);
    }

    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                                     long uploadBandwidth, double hashPower, BitcoinBlockWithoutTx genesisBlock,
                                     ChainBasedConsensusConfig chainBasedConsensusConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower, genesisBlock,
                (NakamotoConsensusConfig) chainBasedConsensusConfig);
    }

    @Override
    public void generateNewBlock() {
        BitcoinBlockWithoutTx canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();

        double weight = this.network.getRandom().sampleExponentialDistribution(1);
        BitcoinBlockWithoutTx bitcoinBlockWithoutTX = BlockFactory.sampleBitcoinBlock(this.simulator,
                this.getNetwork().getRandom(), this, canonicalChainHead,
                canonicalChainHead.getDifficulty(), weight); // TODO: Difficulty adjustment?

        BitcoinCompactBlockWithoutTx compactBlock = new BitcoinCompactBlockWithoutTx(bitcoinBlockWithoutTX);

        this.processIncomingPacket(
                new Packet(
                        this, this, new DataMessage(compactBlock)
                )
        );
    }

    @Override
    public double getHashPower() {
        return this.hashPower;
    }
}
