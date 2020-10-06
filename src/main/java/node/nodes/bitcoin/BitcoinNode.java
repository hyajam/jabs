package main.java.node.nodes.bitcoin;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.NakamotoConsensus;
import main.java.data.Vote;
import main.java.data.bitcoin.BitcoinBlock;
import main.java.data.bitcoin.BitcoinTx;
import main.java.message.InvMessage;
import main.java.message.Packet;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.p2p.BitcoinCoreP2P;
import main.java.simulator.Simulator;

import static main.java.network.TransactionFactory.sampleBitcoinTransaction;

public class BitcoinNode extends BlockchainNode<BitcoinBlock, BitcoinTx> {
    public static final BitcoinBlock BITCOIN_GENESIS_BLOCK =
            new BitcoinBlock(0, 0, 0, null, null);

    public BitcoinNode(Simulator simulator, int nodeID, long downloadBandwidth, long uploadBandwidth) {
        super(simulator, nodeID, downloadBandwidth, uploadBandwidth,
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
        broadcastTxInvMessage(sampleBitcoinTransaction());
    }
}
