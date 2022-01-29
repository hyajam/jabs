package jabs.consensus;

import jabs.blockchain.LocalBlockTree;
import jabs.data.Block;
import jabs.data.Tx;
import jabs.data.Vote;
import jabs.data.pbft.*;
import jabs.message.VoteMessage;
import jabs.network.BlockFactory;
import jabs.node.nodes.Node;
import jabs.node.nodes.pbft.PBFTNode;
import jabs.simulator.Simulator;

import java.util.HashMap;
import java.util.HashSet;

// based on: https://sawtooth.hyperledger.org/docs/pbft/nightly/master/architecture.html
// another good source: http://ug93tad.github.io/pbft/

public class PBFT<B extends Block<B>, T extends Tx<T>> extends AbstractBlockchainConsensus<B, T>
        implements VotingBasedConsensus<B, T>, DeterministicFinalityConsensus<B, T> {
    private final int numAllParticipants;
    private final HashMap<B, HashMap<Node, Vote>> prepareVotes = new HashMap<>();
    private final HashMap<B, HashMap<Node, Vote>> commitVotes = new HashMap<>();
    private final HashSet<B> preparedBlocks = new HashSet<>();
    private final HashSet<B> committedBlocks = new HashSet<>();
    private int currentViewNumber = 0;

    // TODO: View change should be implemented

    private PBFTMode pbftMode = PBFTMode.NORMAL_MODE;
    private PBFTPhase pbftPhase = PBFTPhase.PRE_PREPARING;

    @Override
    public boolean isBlockFinalized(B block) {
        return false;
    }

    @Override
    public boolean isTxFinalized(T tx) {
        return false;
    }

    @Override
    public int getNumOfFinalizedBlocks() {
        return 0;
    }

    @Override
    public int getNumOfFinalizedTxs() {
        return 0;
    }

    public enum PBFTMode {
        NORMAL_MODE,
        VIEW_CHANGE_MODE
    }

    public enum PBFTPhase {
        PRE_PREPARING,
        PREPARING,
        COMMITTING
    }

    public PBFT(Simulator simulator, int numAllParticipants, LocalBlockTree<B> localBlockTree) {
        super(localBlockTree);
        this.numAllParticipants = numAllParticipants;
        this.currentMainChainHead = localBlockTree.getGenesisBlock();
    }

    public void newIncomingVote(Vote vote) {
        if (vote instanceof PBFTBlockVote) { // for the time being, the view change votes are not supported
            PBFTBlockVote<B> blockVote = (PBFTBlockVote<B>) vote;
            B block = blockVote.getBlock();
            switch (blockVote.getVoteType()) {
                case PRE_PREPARE -> {
                    if (!this.localBlockTree.contains(block)) {
                        this.localBlockTree.add(block);
                    }
                    if (this.localBlockTree.getLocalBlock(block).isConnectedToGenesis) {
                        this.pbftPhase = PBFTPhase.PREPARING;
                        this.blockchainNode.broadcastMessage(
                                new VoteMessage(
                                        new PBFTPrepareVote<>(this.blockchainNode, blockVote.getBlock())
                                )
                        );
                    }
                }
                case PREPARE -> checkVotes(blockVote, block, prepareVotes, preparedBlocks, PBFTPhase.COMMITTING);
                case COMMIT -> checkVotes(blockVote, block, commitVotes, committedBlocks, PBFTPhase.PRE_PREPARING);
            }
        }
    }

    private void checkVotes(PBFTBlockVote<B> vote, B block, HashMap<B, HashMap<Node, Vote>> votes, HashSet<B> blocks, PBFTPhase nextStep) {
        if (!blocks.contains(block)) {
            if (!votes.containsKey(block)) { // this the first vote received for this block
                votes.put(block, new HashMap<>());
            }
            votes.get(block).put(vote.getVoter(), vote);
            if (votes.get(block).size() > (((numAllParticipants / 3) * 2) + 1)) {
                blocks.add(block);
                this.pbftPhase = nextStep;
                switch (nextStep) {
                    case PRE_PREPARING -> {
                        this.currentViewNumber += 1;
                        this.currentMainChainHead = block;
                        updateChain();
                        if (this.blockchainNode.nodeID == this.getCurrentPrimaryNumber()){
                            this.blockchainNode.broadcastMessage(
                                    new VoteMessage(
                                            new PBFTPrePrepareVote<>(this.blockchainNode,
                                                    BlockFactory.samplePBFTBlock(blockchainNode.getSimulator(),
                                                            blockchainNode.getNetwork().getRandom(),
                                                            (PBFTNode) this.blockchainNode, (PBFTBlock) block)
                                            )
                                    )
                            );
                        }
                    }
                    case COMMITTING -> this.blockchainNode.broadcastMessage(
                                new VoteMessage(
                                        new PBFTCommitVote<>(this.blockchainNode, block)
                                )
                        );
                }
            }
        }
    }

    @Override
    public void newIncomingBlock(B block) {

    }

    public int getCurrentViewNumber() {
        return this.currentViewNumber;
    }

    public int getCurrentPrimaryNumber() {
        return (this.currentViewNumber % this.numAllParticipants);
    }

    public int getNumAllParticipants() {
        return this.numAllParticipants;
    }

    public PBFTPhase getPbftPhase() {
        return this.pbftPhase;
    }

    @Override
    protected void updateChain() {
        this.acceptedBlocks.add(this.currentMainChainHead);
    }
}
