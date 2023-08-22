package jabs.network.node.nodes.snow;

import jabs.consensus.algorithm.Snow;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.ledgerdata.Query;
import jabs.ledgerdata.Vote;
import jabs.ledgerdata.snow.SnowBlock;
import jabs.ledgerdata.snow.SnowTx;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.network.p2p.SnowP2P;
import jabs.simulator.Simulator;

public class SnowNode extends PeerBlockchainNode<SnowBlock, SnowTx> {
    public static final SnowBlock SNOW_GENESIS_BLOCK =
            new SnowBlock(0, 0, 0, null, null);
    public SnowBlock currentBlock;
    public SnowNode(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, int numAllParticipants) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new SnowP2P(),
                new Snow<>(new LocalBlockTree<>(SNOW_GENESIS_BLOCK), numAllParticipants)
        );
        this.consensusAlgorithm.setNode(this);
    }

    @Override
    protected void processNewTx(SnowTx tx, Node from) {
        // nothing for now
    }

    @Override
    protected void processNewBlock(SnowBlock block) {
        // nothing for now
    }
    @Override
    protected void processNewVote(Vote vote) {

    }
    @Override
    protected void processNewQuery(Query query) {
        ((Snow<SnowBlock, SnowTx>) this.consensusAlgorithm).newIncomingQuery(query);
    }
    @Override
    public void generateNewTransaction() {
        // nothing for now
    }
}
