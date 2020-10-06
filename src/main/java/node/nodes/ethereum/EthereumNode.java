package main.java.node.nodes.ethereum;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.AbstractBlockchainConsensus;
import main.java.consensus.GhostProtocol;
import main.java.consensus.VotingBasedConsensus;
import main.java.data.Vote;
import main.java.data.ethereum.EthereumBlock;
import main.java.data.ethereum.EthereumTx;
import main.java.message.DataMessage;
import main.java.message.InvMessage;
import main.java.message.Packet;
import main.java.message.VoteMessage;
import main.java.network.Network;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.p2p.EthereumGethP2P;
import main.java.random.Random;
import main.java.simulator.Simulator;

import java.util.Collections;

import static main.java.network.BlockFactory.ETHEREUM_MIN_DIFFICULTY;
import static org.apache.commons.math3.util.FastMath.sqrt;

public class EthereumNode extends BlockchainNode<EthereumBlock, EthereumTx> {
    public static final EthereumBlock ETHEREUM_GENESIS_BLOCK =
            new EthereumBlock(0, 0, 0, null, null, Collections.emptySet(),
                    ETHEREUM_MIN_DIFFICULTY);

    public EthereumNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new EthereumGethP2P(),
                new GhostProtocol<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK)));
    }

    public EthereumNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth,
                        AbstractBlockchainConsensus<EthereumBlock, EthereumTx> consensusAlgorithm) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new EthereumGethP2P(),
                consensusAlgorithm);
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
        broadcastTransaction(network.sampleEthereumTransaction());
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
