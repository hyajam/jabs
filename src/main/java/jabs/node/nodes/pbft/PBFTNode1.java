package jabs.node.nodes.pbft;

import jabs.blockchain.LocalBlockTree;
import jabs.consensus.PBFT;
import jabs.data.Vote;
import jabs.data.pbft.PBFTBlock;
import jabs.data.pbft.PBFTTx;
import jabs.network.Network;
import jabs.node.nodes.BlockchainNode;
import jabs.node.nodes.Node;
import jabs.p2p.PBFTP2P;
import jabs.simulator.Simulator;

public class PBFTNode1 extends BlockchainNode<PBFTBlock, PBFTTx> {
                        public static final PBFTBlock PBFT_GENESIS_BLOCK =
            new PBFTBlock(0, 0, 0, null, null);

    public PBFTNode1(Simulator simulator, Network network, int nodeID, long downloadBandwidth, long uploadBandwidth, int numAllParticipants) {
        super(simulator, network, nodeID, downloadBandwidth, uploadBandwidth,
                new PBFTP2P(),
                new PBFT<>(simulator, numAllParticipants, new LocalBlockTree<>(PBFT_GENESIS_BLOCK)));
    }

    @Override
    protected void processNewTx(PBFTTx tx, Node from) {
        // nothing for now
    }

    @Override
    protected void processNewBlock(PBFTBlock block) {
        // nothing for now
    }

    @Override
    protected void processNewVote(Vote vote) {
        ((PBFT) this.consensusAlgorithm).newIncomingVote(vote);
    }

    @Override
    public void generateNewTransaction() {
        // nothing for now
    }
}
