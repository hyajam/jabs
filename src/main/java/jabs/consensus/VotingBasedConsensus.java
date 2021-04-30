package jabs.consensus;

import jabs.data.Block;
import jabs.data.Tx;
import jabs.data.Vote;

public interface VotingBasedConsensus<B extends Block<B>, T extends Tx<T>> extends ConsensusAlgorithm<B, T> {
    void newIncomingVote(Vote vote);
}
