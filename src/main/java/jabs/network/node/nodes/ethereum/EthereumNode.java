package jabs.network.node.nodes.ethereum;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.algorithm.GhostProtocol;
import jabs.consensus.algorithm.VotingBasedConsensus;
import jabs.ledgerdata.Vote;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.ledgerdata.ethereum.EthereumTx;
import jabs.network.message.DataMessage;
import jabs.network.message.InvMessage;
import jabs.network.message.Packet;
import jabs.network.message.VoteMessage;
import jabs.network.networks.Network;
import jabs.network.networks.TransactionFactory;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.network.node.nodes.Node;
import jabs.network.p2p.EthereumGethP2P;
import jabs.simulator.Simulator;

import java.util.Collections;

import static jabs.network.networks.BlockFactory.ETHEREUM_MIN_DIFFICULTY;
import static org.apache.commons.math3.util.FastMath.sqrt;

public class EthereumNode extends PeerBlockchainNode<EthereumBlock, EthereumTx> {
    public static final EthereumBlock ETHEREUM_GENESIS_BLOCK =
            new EthereumBlock(0, 0, 0, null, null, Collections.emptySet(),
                    ETHEREUM_MIN_DIFFICULTY);

    public EthereumNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new EthereumGethP2P(),
                new GhostProtocol<>(new LocalBlockTree<>(ETHEREUM_GENESIS_BLOCK)));
    }

    public EthereumNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth,
                        AbstractChainBasedConsensus<EthereumBlock, EthereumTx> consensusAlgorithm) {
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
        broadcastTransaction(TransactionFactory.sampleEthereumTransaction(network.getRandom()));
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
