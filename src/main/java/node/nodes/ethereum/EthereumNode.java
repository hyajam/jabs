package main.java.node.nodes.ethereum;

import main.java.consensus.CasperFFG;
import main.java.consensus.DAGsper;
import main.java.consensus.VotingBasedConsensus;
import main.java.data.Vote;
import main.java.data.ethereum.EthereumBlock;
import main.java.data.ethereum.EthereumTx;
import main.java.message.InvMessage;
import main.java.message.DataMessage;
import main.java.blockchain.LocalBlockTree;
import main.java.consensus.GhostProtocol;
import main.java.message.Packet;
import main.java.message.VoteMessage;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.p2p.EthereumGethP2P;

import java.util.Collections;

import static main.java.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;
import static main.java.network.TransactionFactory.sampleEthereumTransaction;
import static org.apache.commons.math3.util.FastMath.sqrt;

public class EthereumNode extends BlockchainNode<EthereumBlock, EthereumTx> {
    public static final EthereumBlock ETHEREUM_GENESIS_BLOCK =
            new EthereumBlock(0, 0, 0, null, null, Collections.emptySet(),
                    ETHEREUM_MIN_DIFFICULTY);

    public EthereumNode(int nodeID, int region) {
        super(nodeID, region,
                new EthereumGethP2P(),
                new CasperFFG<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK), 3, 40));
    }

    @Override
    protected void processNewTx(EthereumTx ethereumTx, Node from) {
        this.broadcastTransaction(ethereumTx, from);
    }

    @Override
    protected void processNewBlock(EthereumBlock ethereumBlock) {
        this.consensusAlgorithm.newIncomingBlock(ethereumBlock);
        this.broadcastNewBlockAndBlockHashes(ethereumBlock);
    }

    @Override
    protected void processNewVote(Vote vote) {
        if (this.consensusAlgorithm instanceof VotingBasedConsensus) {
            ((VotingBasedConsensus) this.consensusAlgorithm).newIncomingVote(vote);
            for (Node neighbor : this.p2pConnections.getNeighbors()) {
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new Packet(this, neighbor,
                                new VoteMessage(vote)
                        )
                );
            }
        }
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
                        new Packet(this, neighbor,
                                new DataMessage(ethereumBlock)
                        )
                );
            } else {
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new Packet(this, neighbor,
                                new InvMessage(ethereumBlock.getHash().getSize(), ethereumBlock.getHash())
                        )
                );
            }
        }
    }

    protected void broadcastTransaction(EthereumTx tx, Node excludeNeighbor) {
        for (Node neighbor:this.p2pConnections.getNeighbors()) {
            if (neighbor != excludeNeighbor){
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new Packet(this, neighbor,
                                new DataMessage(tx)
                        )
                );
            }
        }
    }

    protected void broadcastTransaction(EthereumTx tx) {
        broadcastTransaction(tx, null);
    }

}
