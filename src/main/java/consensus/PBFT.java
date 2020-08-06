package main.java.consensus;

import main.java.data.Block;
import main.java.data.Transaction;
import main.java.data.Vote;
import main.java.data.pbft.PBFTBlockVote;
import main.java.node.nodes.Node;

import java.util.HashMap;
import java.util.HashSet;

public class PBFT<B extends Block<B>, T extends Transaction<T>> extends AbstractConsensusAlgorithm<B, T> {
    private final int numAllParticipants;
    private HashMap<B, HashMap<Node, Vote>> prepareVotes = new HashMap<>();
    private HashMap<B, HashMap<Node, Vote>> commitVotes = new HashMap<>();
    private HashSet<B> preparedBlocks = new HashSet<>();
    private HashSet<B> committedBlocks = new HashSet<>();

    public PBFT(int numAllParticipants) {
        this.numAllParticipants = numAllParticipants;
    }

    public void newIncomingVote(Vote vote) {
        if (vote instanceof PBFTBlockVote) {
            PBFTBlockVote<B> blockVote = (PBFTBlockVote<B>) vote;
            B block = blockVote.getBlock();
            switch (blockVote.getVoteType()) {
                case COMMIT -> {
                    if (!committedBlocks.contains(block)) {
                        if (!commitVotes.containsKey(block)) { // this the first commit vote received for this block
                            commitVotes.put(block, null);
                        }
                        commitVotes.get(block).put(blockVote.getVoter(), blockVote);
                        if (commitVotes.get(block).size() > (((numAllParticipants / 3) * 2) + 1) ) {
                            committedBlocks.add(block);
                            commitVotes.remove(block);
                        }
                    }
                }
                case PREPARE -> {

                }
                case PRE_PREPARE -> {
                    if (!preparedBlocks.contains(block)) {
                        if (!prepareVotes.containsKey(block)) { // this the first prepare vote received for this block
                            prepareVotes.put(block, null);
                        }
                        prepareVotes.get(block).put(blockVote.getVoter(), blockVote);
                        if (prepareVotes.get(block).size() > ((numAllParticipants / 3) * 2) ) {
                            preparedBlocks.add(block);
                            prepareVotes.remove(block);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void newBlock(B block) {

    }

    private void blockPrepared() {

    }

    private void blockCommitted() {

    }

    public int getNumAllParticipants() {
        return this.numAllParticipants;
    }
}
