package jabs.consensus.algorithm;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.consensus.config.DAGsperConfig;
import jabs.ledgerdata.*;
import jabs.ledgerdata.dagsper.DAGsperVote;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.network.message.Packet;
import jabs.network.message.VoteMessage;
import jabs.network.node.nodes.Node;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.*;

public class DAGsper<B extends SingleParentBlock<B>, T extends Tx<T>> extends GhostProtocol<B, T>
        implements VotingBasedConsensus<B, T>, DeterministicFinalityConsensus<B,T> {
    private final int numOfStakeholders;
    private final int checkpointSpace;

    private final HashMap<Integer, HashMap<Node, B>> fVotesInHeight = new HashMap<>();
    private final HashMap<Integer, HashMap<Node, B>> jVotesInHeight = new HashMap<>();
    private final HashMap<B, HashSet<Node>> fVotesForBlock = new HashMap<>();
    private final HashMap<B, HashSet<Node>> jVotesForBlock = new HashMap<>();
    private final HashMap<Integer, HashSet<B>> fVotedBlocksInHeight = new HashMap<>();
    private final HashMap<Integer, HashSet<B>> jVotedBlocksInHeight = new HashMap<>();
    private final Set<Integer> unfinalizableHeights = new HashSet<>();

    private final SortedSet<B> finalizedBlocks = new TreeSet<>();
    private final SortedSet<B> justifiedBlocks = new TreeSet<>();
    private final Set<B> indirectlyFinalizedBlocks = new HashSet<>();
    private final Set<T> finalizedTxs = new HashSet<>();

    private final HashMap<Integer, B> previousFVotes = new HashMap<>();
    private final HashMap<Integer, B> previousJVotes = new HashMap<>();

    private DescriptiveStatistics blockFinalizationTimes = null;

    public DAGsper(LocalBlockTree<B> localBlockTree, DAGsperConfig daGsperConfig) {
        super(localBlockTree, daGsperConfig);
        this.checkpointSpace = daGsperConfig.checkpointSpace();
        this.numOfStakeholders = daGsperConfig.numOfStakeholders();
        finalizedBlocks.add(localBlockTree.getGenesisBlock());
        justifiedBlocks.add(localBlockTree.getGenesisBlock());
        indirectlyFinalizedBlocks.add(localBlockTree.getGenesisBlock());
    }

    public void enableFinalizationTimeRecords(DescriptiveStatistics blockFinalizationTimes) {
        this.blockFinalizationTimes = blockFinalizationTimes;
    }

    @Override
    public void newIncomingVote(Vote vote) {
        if (vote instanceof DAGsperVote) {
            B toBeJustified = (B) ((DAGsperVote) vote).toBeJustified;
            B latestFinalized = localBlockTree.getAncestorOfHeight(toBeJustified, ((DAGsperVote) vote).latestFinalizedHeight);
            B toBeFinalized = localBlockTree.getAncestorOfHeight(toBeJustified, ((DAGsperVote) vote).toBeFinalizedHeight);
            HashSet<Integer> unaffectedHeights = ((DAGsperVote) vote).unaffectedHeights;
            List<B> toBeJustifiedPath = localBlockTree.getPathBetween(toBeJustified, toBeFinalized);
            if (toBeJustifiedPath != null) {
                toBeJustifiedPath.removeIf(i -> unaffectedHeights.contains(i.getHeight()));
                this.addVote(toBeJustifiedPath, vote.getVoter(), jVotesInHeight, jVotesForBlock, fVotedBlocksInHeight);
            }
            List<B> toBeFinalizedPath = localBlockTree.getPathBetween(toBeFinalized, latestFinalized);
            if (toBeFinalizedPath != null) {
                toBeFinalizedPath.removeIf(i -> unaffectedHeights.contains(i.getHeight()));
                this.addVote(toBeFinalizedPath, vote.getVoter(), fVotesInHeight, fVotesForBlock, jVotedBlocksInHeight);
            }
        }
    }

    public void addVote(List<B> blocks, Node voter, HashMap<Integer, HashMap<Node, B>> heightVotes,
                        HashMap<B, HashSet<Node>> blockVotes, HashMap<Integer, HashSet<B>> blocksInHeight) {
        for (B block:blocks) {
            if (!heightVotes.containsKey(block.getHeight())) { // first vote for this height
                heightVotes.put(block.getHeight(), new HashMap<>());
            }

            if (!blockVotes.containsKey(block)) { // first vote for this block
                blockVotes.put(block, new HashSet<>());
            }

            if (!blocksInHeight.containsKey(block.getHeight())) { // first block for this height
                blocksInHeight.put(block.getHeight(), new HashSet<>());
            }

            if (heightVotes.get(block.getHeight()).containsKey(voter)) {
                if (heightVotes.get(block.getHeight()).get(voter) != block) {
                    System.out.printf("Slashing condition for participant %s, on height %s\n", voter, block.getHeight());
                }
            } else {
                heightVotes.get(block.getHeight()).put(voter, block);
                blockVotes.get(block).add(voter);
                blocksInHeight.get(block.getHeight()).add(block);
            }

            if (heightVotes == fVotesInHeight) { // if it is a finalization vote
                int totalNumOfVotes = heightVotes.get(block.getHeight()).size();
                int highestVote = 0;
                for (B blockOfHeight:blocksInHeight.get(block.getHeight())) {
                    if (highestVote < blockVotes.get(blockOfHeight).size()) {
                        highestVote = blockVotes.get(blockOfHeight).size();
                    }
                }
                if ((numOfStakeholders - totalNumOfVotes) + highestVote < (((numOfStakeholders / 3) * 2) + 1)) {
                    // if the height is unfinalizable
                    unfinalizableHeights.add(block.getHeight());
                }

                // did block gain enough votes?
                if (!finalizedBlocks.contains(block)) {
                    if (blockVotes.get(block).size() > (((numOfStakeholders / 3) * 2) + 1)) {
                        int finalizableHeight = block.getHeight() - 1;

                        while (unfinalizableHeights.contains(finalizableHeight)) {
                            finalizableHeight--;
                        }

                        if (finalizedBlocks.contains(localBlockTree.getAncestorOfHeight(block, finalizableHeight))) {
                            finalizedBlocks.add(block);
                            updateFinalizedBlocks(finalizedBlocks.last());
                            this.originOfGhost = finalizedBlocks.last();
                        }
                    }
                }
            } else { // it is a justification vote
                // did block gain enough votes?
                if (blockVotes.get(block).size() > (((numOfStakeholders / 3) * 2) + 1)) {
                    justifiedBlocks.add(block);
                }
            }
        }
    }

    private void updateFinalizedBlocks(B newlyFinalizedBlock) {
        if (!indirectlyFinalizedBlocks.contains(newlyFinalizedBlock)) {
            indirectlyFinalizedBlocks.add(newlyFinalizedBlock);
            if (blockFinalizationTimes != null) {
                blockFinalizationTimes.addValue(peerBlockchainNode.getSimulator().getCurrentTime() - newlyFinalizedBlock.getCreationTime());
            }
        }

        Set<B> ancestors = new HashSet<>();
        Set<B> nextAncestors = new HashSet<>();
        ancestors.add(newlyFinalizedBlock);
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
                    if (!indirectlyFinalizedBlocks.contains(block)) {
                        ancestors.add(block);
                    }
                }
            }

            indirectlyFinalizedBlocks.addAll(ancestors);

            for (B block:ancestors) {
                if (blockFinalizationTimes != null) {
                    blockFinalizationTimes.addValue(peerBlockchainNode.getSimulator().getCurrentTime() - block.getCreationTime());
                }
                if (block instanceof BlockWithTx) {
                    finalizedTxs.addAll(((BlockWithTx<T>) block).getTxs());
                }
            }

            nextAncestors.clear();
        }
    }

    @Override
    protected void updateChain() {
        confirmedBlocks = localBlockTree.getAllAncestors(currentMainChainHead);
        if (currentMainChainHead.getHeight() > 0) {
            if ((currentMainChainHead.getHeight() % checkpointSpace) == (peerBlockchainNode.nodeID % checkpointSpace)) {
                B latestConnectedJBlock = finalizedBlocks.last();

                if (localBlockTree.areBlocksConnected(currentMainChainHead, justifiedBlocks.last())) {
                    latestConnectedJBlock = justifiedBlocks.last();
                } else {
                    for (B jBlock:justifiedBlocks) {
                        if (localBlockTree.areBlocksConnected(currentMainChainHead, jBlock))
                            latestConnectedJBlock = jBlock;
                    }
                }

                HashSet<Integer> unaffectedHeights = new HashSet<>();

                List<B> fPath = localBlockTree.getPathBetween(latestConnectedJBlock, finalizedBlocks.last());
                removePreviouslyVotedHeights(unaffectedHeights, fPath, previousFVotes);

                List<B> jPath = localBlockTree.getPathBetween(currentMainChainHead, latestConnectedJBlock);
                removePreviouslyVotedHeights(unaffectedHeights, jPath, previousJVotes);

                DAGsperVote<B> daGsperVote = new DAGsperVote<>(peerBlockchainNode, finalizedBlocks.last().getHeight(),
                        latestConnectedJBlock.getHeight(), currentMainChainHead, unaffectedHeights);
                VoteMessage voteMessage = new VoteMessage(daGsperVote);
                Packet packet = new Packet(this.peerBlockchainNode, this.peerBlockchainNode, voteMessage);
                this.peerBlockchainNode.processIncomingPacket(packet);
            }
        }
    }

    private void removePreviouslyVotedHeights(HashSet<Integer> unaffectedHeights, List<B> path, HashMap<Integer, B> previousVotes) {
        if (path != null) {
            for (B jBlock:path) {
                if (previousVotes.containsKey(jBlock.getHeight())) {
                    if (previousVotes.get(jBlock.getHeight()) != jBlock) {
                        unaffectedHeights.add(jBlock.getHeight());
                    }
                } else {
                    previousVotes.put(jBlock.getHeight(), jBlock);
                }
            }
        }
    }

    @Override
    public boolean isBlockFinalized(B block) {
        return this.indirectlyFinalizedBlocks.contains(block);
    }

    @Override
    public boolean isTxFinalized(T tx) {
        return this.finalizedTxs.contains(tx);
    }

    @Override
    public int getNumOfFinalizedBlocks() {
        return this.indirectlyFinalizedBlocks.size();
    }

    @Override
    public int getNumOfFinalizedTxs() {
        return this.finalizedTxs.size();
    }
}
