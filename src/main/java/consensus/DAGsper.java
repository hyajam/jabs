package main.java.consensus;

import main.java.blockchain.LocalBlockTree;
import main.java.data.Block;
import main.java.data.BlockWithTx;
import main.java.data.Tx;
import main.java.data.Vote;
import main.java.data.dagsper.DAGsperVote;
import main.java.data.ethereum.EthereumBlock;
import main.java.message.Packet;
import main.java.message.VoteMessage;
import main.java.node.nodes.Node;

import java.util.*;

public class DAGsper<B extends Block<B>, T extends Tx<T>> extends GhostProtocol<B, T>
        implements VotingBasedConsensus<B, T>, DeterministicFinalityConsensus<B,T> {
    private final HashMap<Integer, HashMap<B, HashSet<Node>>> finalizationVotes = new HashMap<>();
    private final HashMap<Integer, HashMap<B, HashSet<Node>>> justificationVotes = new HashMap<>();
    private final int numOfStakeholders;
    private final Set<Integer> unfinalizableHeights = new HashSet<>();
    private B latestFinalizedBlock;
    private B latestJustifiedBlock;
    private final int checkpointSpace;
    public final Set<B> finalizedBlocks = new HashSet<>();
    public final Set<T> finalizedTxs = new HashSet<>();

    public DAGsper(LocalBlockTree<B> localBlockTree, int checkpointSpace, int numOfStakeholders) {
        super(localBlockTree);
        this.checkpointSpace = checkpointSpace;
        this.numOfStakeholders = numOfStakeholders;
        latestFinalizedBlock = localBlockTree.getGenesisBlock();
        latestJustifiedBlock = localBlockTree.getGenesisBlock();
    }

    @Override
    public void newIncomingVote(Vote vote) {
        if (vote instanceof DAGsperVote) {
            B latestFinalized = (B) ((DAGsperVote) vote).latestFinalized;
            B toBeFinalized = (B) ((DAGsperVote) vote).toBeFinalized;
            B toBeJustified = (B) ((DAGsperVote) vote).toBeJustified;
            List<B> toBeFinalizedPath = localBlockTree.getPathBetween(toBeFinalized, latestFinalized);
            if (toBeFinalizedPath != null) {
                this.addVote(toBeFinalizedPath, vote.getVoter(), finalizationVotes);
            }
            List<B> toBeJustifiedPath = localBlockTree.getPathBetween(toBeJustified, toBeFinalized);
            if (toBeJustifiedPath != null) {
                this.addVote(toBeJustifiedPath, vote.getVoter(), justificationVotes);
            }
        }
    }

    public void addVote(List<B> blocks, Node voter, HashMap<Integer, HashMap<B, HashSet<Node>>> votes) {
        for (B block:blocks) {
            if (!votes.containsKey(block.getHeight())) { // first vote for this height
                votes.put(block.getHeight(), new HashMap<>());
            }

            if (!votes.get(block.getHeight()).containsKey(block)) {  // first vote for this block
                votes.get(block.getHeight()).put(block, new HashSet<>());
            }

            // TODO: Slashing
            votes.get(block.getHeight()).get(block).add(voter);

            if (votes == finalizationVotes) { // if it is a finalization vote
                int totalNumOfVotes = 0;
                int highestVotes = 0;
                for (B block1:votes.get(block.getHeight()).keySet()) {
                    totalNumOfVotes += votes.get(block.getHeight()).get(block1).size();
                    if (highestVotes < votes.get(block.getHeight()).get(block1).size()) {
                        highestVotes = votes.get(block.getHeight()).get(block1).size();
                    }
                }
                if ((numOfStakeholders - totalNumOfVotes) + highestVotes < (((numOfStakeholders / 3) * 2) + 1)) {
                    // if the height is unfinalizable
                    unfinalizableHeights.add(block.getHeight());
                }

                // did block gain enough votes?
                if (votes.get(block.getHeight()).get(block).size() > (((numOfStakeholders / 3) * 2) + 1)) {
                    int finalizableHeight = block.getHeight() - 1;
                    while (unfinalizableHeights.contains(finalizableHeight)) {
                        finalizableHeight--;
                    }
                    if (localBlockTree.getAncestorOfHeight(block, finalizableHeight) == latestFinalizedBlock) {
                        latestFinalizedBlock = block;
                        updateFinalizedBlocks();
                    }
                }
            } else {
                // did block gain enough votes?
                if (votes.get(block.getHeight()).get(block).size() > (((numOfStakeholders / 3) * 2) + 1)) {
                    if (block.getHeight() > latestJustifiedBlock.getHeight()) {
                        latestJustifiedBlock = block;
                    }
                }
            }
        }
    }

    protected void updateFinalizedBlocks() {
        finalizedBlocks.add(latestFinalizedBlock);

        Set<B> ancestors = new HashSet<>();
        Set<B> nextAncestors = new HashSet<>();
        ancestors.add(latestFinalizedBlock);
        while (ancestors.size() > 0) {
            for (B block:ancestors) {
                nextAncestors.add(block.getParent());
                if (block instanceof EthereumBlock) {
                    nextAncestors.addAll((Collection<? extends B>) ((EthereumBlock) block).getUncles());
                }
            }
            ancestors.clear();
            for (B block:nextAncestors) {
                if (localBlockTree.contains(block)) {
                    if (!finalizedBlocks.contains(block)) {
                        ancestors.add(block);
                    }
                }
            }
            finalizedBlocks.addAll(ancestors);
            for (B block:ancestors) {
                if (block instanceof BlockWithTx) {
                    finalizedTxs.addAll(((BlockWithTx<T>) block).getTxs());
                }
            }
            nextAncestors.clear();
        }
    }

    @Override
    protected void updateChain() {
        acceptedBlocks = localBlockTree.getAllAncestors(currentMainChainHead);
        if (currentMainChainHead.getHeight() > 0) {
            if ((currentMainChainHead.getHeight() % checkpointSpace) == (blockchainNode.nodeID % checkpointSpace)) {
                DAGsperVote<B> daGsperVote = new DAGsperVote<>(blockchainNode, latestFinalizedBlock,
                        latestJustifiedBlock, currentMainChainHead);
                VoteMessage voteMessage = new VoteMessage(daGsperVote);
                Packet packet = new Packet(this.blockchainNode, this.blockchainNode, voteMessage);
                this.blockchainNode.processIncomingPacket(packet);
            }
        }
    }

    public int isBlockFinalized() {
        return this.finalizedBlocks.size();
    }

    @Override
    public boolean isBlockFinalized(B block) {
        return this.finalizedBlocks.contains(block);
    }

    @Override
    public boolean isTxFinalized(T tx) {
        return this.finalizedTxs.contains(tx);
    }

    public int getNumOfFinalizedBlocks() {
        return this.finalizedBlocks.size();
    }

    @Override
    public int getNumOfFinalizedTxs() {
        return this.finalizedTxs.size();
    }
}
