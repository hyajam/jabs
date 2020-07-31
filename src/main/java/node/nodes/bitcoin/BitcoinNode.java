package main.java.node.nodes.bitcoin;

import main.java.data.bitcoin.*;
import main.java.message.BlockInvMessage;
import main.java.message.TxInvMessage;
import main.java.blockchain.LocalBlockTree;
import main.java.consensus.NakamotoConsensus;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.p2p.BitcoinCoreP2P;

import static main.java.network.TransactionFactory.sampleBitcoinTransaction;

public class BitcoinNode extends BlockchainNode<BitcoinTx, BitcoinBlock> {
    public static final BitcoinBlock BITCOIN_GENESIS_BLOCK =
            new BitcoinBlock(0, 0, 0, null, null);

    public BitcoinNode(int nodeID, int region) {
        super(nodeID, region,
                new BitcoinCoreP2P(),
                new NakamotoConsensus<>(new LocalBlockTree<>(BITCOIN_GENESIS_BLOCK)));
    }

    @Override
    protected void processNewTx(BitcoinTx bitcoinTx, Node from) {
        this.broadcastTxInvMessage(bitcoinTx);
    }

    @Override
    protected void processNewBlock(BitcoinBlock bitcoinBlock) {
        this.consensusAlgorithm.newBlock(bitcoinBlock);
        this.broadcastBlockInvMessage(bitcoinBlock);
    }

    protected void broadcastTxInvMessage(BitcoinTx tx) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.nodeNetworkInterface.addToUpLinkQueue(
                    new TxInvMessage<>(tx.getSize(), this, neighbor, tx.getHash())
            );
        }
    }

    protected void broadcastBlockInvMessage(BitcoinBlock block) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            this.nodeNetworkInterface.addToUpLinkQueue(
                    new BlockInvMessage<>(block.getSize(), this, neighbor, block.getHash())
            );
        }
    }

    @Override
    public void generateNewTransaction() {
        broadcastTxInvMessage(sampleBitcoinTransaction());
    }
}
