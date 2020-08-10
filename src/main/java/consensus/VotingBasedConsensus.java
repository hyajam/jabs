package main.java.consensus;

import main.java.data.Block;
import main.java.data.Tx;
import main.java.data.Vote;

public interface VotingBasedConsensus<B extends Block<B>, T extends Tx<T>> extends ConsensusAlgorithm<B, T> {
    void newIncomingVote(Vote vote);
}
