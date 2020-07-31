package main.java.node.nodes.ethereum;

import main.java.data.ethereum.EthereumBlock;
import main.java.data.ethereum.EthereumTx;
import main.java.message.BlockInvMessage;
import main.java.message.BlockMessage;
import main.java.message.TxMessage;
import main.java.blockchain.LocalBlockTree;
import main.java.consensus.GhostProtocol;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.p2p.EthereumGethP2P;

import java.util.Collections;

import static main.java.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;
import static main.java.network.TransactionFactory.sampleEthereumTransaction;
import static org.apache.commons.math3.util.FastMath.sqrt;

public class EthereumNode extends BlockchainNode<EthereumTx, EthereumBlock> {
    public static final EthereumBlock ETHEREUM_GENESIS_BLOCK =
            new EthereumBlock(0, 0, 0, null, null, Collections.emptySet(),
                    ETHEREUM_MIN_DIFFICULTY);

    public EthereumNode(int nodeID, int region) {
        super(nodeID, region,
                new EthereumGethP2P(),
                new GhostProtocol<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK)));
    }

    @Override
    protected void processNewTx(EthereumTx ethereumTx, Node from) {
        this.broadcastTransaction(ethereumTx, from);
    }

    @Override
    protected void processNewBlock(EthereumBlock ethereumBlock) {
        this.consensusAlgorithm.newBlock(ethereumBlock);
        this.broadcastNewBlockAndBlockHashes(ethereumBlock);
    }

    @Override
    public void generateNewTransaction() {
        broadcastTransaction(sampleEthereumTransaction());
    }

    protected void broadcastNewBlockAndBlockHashes(EthereumBlock ethereumBlock){
        for (int i = 0; i < this.p2pConnections.getNeighbors().size(); i++) {
            Node neighbor = this.p2pConnections.getNeighbors().get(i);
            if (i < sqrt(this.p2pConnections.getNeighbors().size())){
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new BlockMessage<>(ethereumBlock.getSize(), this, neighbor, ethereumBlock));
            } else {
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new BlockInvMessage<>(ethereumBlock.getSize(), this, neighbor, ethereumBlock.getHash()));
            }
        }
    }

    protected void broadcastTransaction(EthereumTx tx, Node excludeNeighbor) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            if (neighbor != excludeNeighbor){
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new TxMessage<>(tx.getSize(), this, neighbor, tx)
                );
            }
        }
    }

    protected void broadcastTransaction(EthereumTx tx) {
        broadcastTransaction(tx, null);
    }

}
