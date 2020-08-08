package main.java.data.pbft;

import main.java.data.Block;
import main.java.node.nodes.Node;

public class PBFTCommitVote<B extends Block<B>> extends PBFTBlockVote<B> {
    public PBFTCommitVote(Node voter, B block) {
        super(block.getHash().getSize() + PBFT_VOTE_SIZE_OVERHEAD, voter, block, VoteType.COMMIT);
    }
}
