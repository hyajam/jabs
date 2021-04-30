package jabs.node.nodes.bitcoin;

import jabs.blockchain.LocalBlockTree;
import jabs.consensus.NakamotoConsensus;
import jabs.data.Vote;
import jabs.data.bitcoin.BitcoinBlock;
import jabs.data.bitcoin.BitcoinTx;
import jabs.message.InvMessage;
import jabs.message.Packet;
import jabs.network.Network;
import jabs.network.TransactionFactory;
import jabs.node.nodes.BlockchainNode;
import jabs.node.nodes.Node;
import jabs.p2p.BitcoinCoreP2P;
import jabs.simulator.Simulator;

public class BitcoinNode extends BlockchainNode<BitcoinBlock, BitcoinTx> {
    public static final BitcoinBlock BITCOIN_GENESIS_BLOCK =
            new BitcoinBlock(0, 0, 0, null, null);

    public BitcoinNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new BitcoinCoreP2P(),
                new NakamotoConsensus<>(new LocalBlockTree<>(BITCOIN_GENESIS_BLOCK)));
    }

    @Override
    protected void processNewTx(BitcoinTx bitcoinTx, Node from) {
        this.broadcastTxInvMessage(bitcoinTx);
    }

    @Override
    protected void processNewBlock(BitcoinBlock bitcoinBlock) {
        this.consensusAlgorithm.newIncomingBlock(bitcoinBlock);
        this.broadcastBlockInvMessage(bitcoinBlock);
    }

    @Override
    protected void processNewVote(Vote vote) {

    }

    protected void broadcastTxInvMessage(BitcoinTx tx) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.nodeNetworkInterface.addToUpLinkQueue(
                    new Packet(this, neighbor,
                            new InvMessage(tx.getHash().getSize(), tx.getHash())
                    )
            );
        }
    }

    protected void broadcastBlockInvMessage(BitcoinBlock block) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.nodeNetworkInterface.addToUpLinkQueue(
                    new Packet(this, neighbor,
                            new InvMessage(block.getHash().getSize(), block.getHash())
                    )
            );
        }
    }

    @Override
    public void generateNewTransaction() {
        broadcastTxInvMessage(TransactionFactory.sampleBitcoinTransaction(network.getRandom()));
    }
}
