package jabs.network.node.nodes.bitcoin;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.ledgerdata.bitcoin.BitcoinBlock;
import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.network.message.DataMessage;
import jabs.network.message.Packet;
import jabs.ledgerdata.BlockFactory;
import jabs.network.networks.Network;
import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;


public class BitcoinMinerNodeWithoutTx extends BitcoinMinerNode implements MinerNode {
    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                                     long uploadBandwidth, long hashPower, BitcoinBlock genesisBlock,
                                     AbstractChainBasedConsensus<BitcoinBlock, BitcoinTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, genesisBlock, hashPower,
                consensusAlgorithm);
    }

    public BitcoinMinerNodeWithoutTx(Simulator simulator, Network network, int nodeID, long downloadBandwidth,
                                     long uploadBandwidth, long hashPower, BitcoinBlock genesisBlock,
                                     ChainBasedConsensusConfig chainBasedConsensusConfig) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth, hashPower, genesisBlock,
                (NakamotoConsensusConfig) chainBasedConsensusConfig);
    }

    @Override
    public void generateNewBlock() {
        BitcoinBlock canonicalChainHead = this.consensusAlgorithm.getCanonicalChainHead();

        BitcoinBlock bitcoinBlock = BlockFactory.sampleBitcoinBlock(this.simulator,
                this.getNetwork().getRandom(), this, canonicalChainHead,
                canonicalChainHead.getDifficulty()); // TODO: Difficulty adjustment?

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
