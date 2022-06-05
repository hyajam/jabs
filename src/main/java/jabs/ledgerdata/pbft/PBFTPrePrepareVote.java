package jabs.ledgerdata.pbft;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;

public class PBFTPrePrepareVote<B extends Block<B>> extends PBFTBlockVote<B> {
    public PBFTPrePrepareVote(Node voter, B block) {
        super(block.getSize() + PBFT_VOTE_SIZE_OVERHEAD, voter, block, VoteType.PRE_PREPARE);
    }
}
