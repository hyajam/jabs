package main.java.node.nodes.pbft;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.PBFT;
import main.java.data.Vote;
import main.java.data.pbft.PBFTBlock;
import main.java.data.pbft.PBFTTx;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.p2p.PBFTP2P;
import main.java.simulator.Simulator;

public class PBFTNode extends BlockchainNode<PBFTBlock, PBFTTx> {
                        public static final PBFTBlock PBFT_GENESIS_BLOCK =
            new PBFTBlock(0, 0, 0, null, null);

    public PBFTNode(Simulator simulator, int nodeID, long downloadBandwidth, long uploadBandwidth, int numAllParticipants) {
        super(simulator, nodeID, downloadBandwidth, uploadBandwidth,
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
