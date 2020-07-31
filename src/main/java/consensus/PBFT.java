package main.java.consensus;

import main.java.data.Block;
import main.java.data.Transaction;

import java.util.HashSet;

public class PBFT<B extends Block<B>, T extends Transaction<T>> extends AbstractConsensusAlgorithm<B, T> {
    private final int numAllParticipants;
//    private HashSet<B, HashSet<>> prepareVote = new HashSet<>();

    public PBFT(int numAllParticipants) {
        this.numAllParticipants = numAllParticipants;
    }

    public void newIncomingVote() {

    }

    @Override
    public void newBlock(B block) {

    }

    @Override
    public B getCanonicalChainHead() {
        return null;
    }
}
