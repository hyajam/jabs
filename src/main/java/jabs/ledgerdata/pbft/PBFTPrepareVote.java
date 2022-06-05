package jabs.ledgerdata.pbft;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

public class PBFTPrepareVote<B extends Block<B>> extends PBFTBlockVote<B> {
    public PBFTPrepareVote(Node voter, B block) {
        super(block.getHash().getSize() + PBFT_VOTE_SIZE_OVERHEAD, voter, block, VoteType.PREPARE);
    }
}
