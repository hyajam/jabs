package main.java.data.pbft;

import main.java.consensus.PBFT;
import main.java.data.Block;
import main.java.node.nodes.Node;

public class PBFTPrepareVote<B extends Block<B>> extends PBFTBlockVote<B> {
    public PBFTPrepareVote(Node voter, B block) {
        super(block.getHash().getSize() + PBFT_VOTE_SIZE_OVERHEAD, voter, block, VoteType.PREPARE);
    }
}
