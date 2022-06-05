package jabs.consensus.algorithm;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;
import jabs.ledgerdata.Vote;

public interface VotingBasedConsensus<B extends Block<B>, T extends Tx<T>> extends ConsensusAlgorithm<B, T> {
    void newIncomingVote(Vote vote);
}
