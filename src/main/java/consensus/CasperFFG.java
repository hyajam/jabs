package main.java.consensus;

import main.java.data.Block;
import main.java.data.BlockWithTx;
import main.java.data.Tx;
import main.java.blockchain.LocalBlockTree;
import main.java.data.Vote;
import main.java.data.casper.CasperFFGLink;
import main.java.data.casper.CasperFFGVote;
import main.java.message.Packet;
import main.java.message.VoteMessage;
import main.java.node.nodes.Node;

import java.util.*;

public class CasperFFG<B extends Block<B>, T extends Tx<T>> extends GhostProtocol<B, T>
        implements VotingBasedConsensus<B, T>, DeterministicFinalityConsensus<B,T> {
    private final HashMap<CasperFFGLink<B>, HashMap<Node, CasperFFGVote<B>>> votes = new HashMap<>();
    private final SortedSet<B> justifiedBlocks = new TreeSet<>();
    private final SortedSet<B> finalizedBlocks = new TreeSet<>();
    private final Set<T> finalizedTxs = new HashSet<>();
    private final int checkpointSpace;
    private final int numOfStakeholders;
    private int latestCheckpoint = 0;

    public CasperFFG(LocalBlockTree<B> localBlockTree, int checkpointSpace, int numOfStakeholders) {
        super(localBlockTree);
        this.checkpointSpace = checkpointSpace;
        this.numOfStakeholders = numOfStakeholders;
        this.justifiedBlocks.add(localBlockTree.getGenesisBlock());
        this.finalizedBlocks.add(localBlockTree.getGenesisBlock());
    }

    @Override
    public void newIncomingVote(Vote vote) {
        if (vote instanceof CasperFFGVote) {
            CasperFFGVote<B> casperFFGVote = (CasperFFGVote<B>) vote;
            CasperFFGLink<B> casperFFGLink = casperFFGVote.getLink();
            if (!votes.containsKey(casperFFGLink)) { // first vote for the link
                votes.put(casperFFGLink, new HashMap<>());
            }
            votes.get(casperFFGLink).put(casperFFGVote.getVoter(), casperFFGVote);
            if (votes.get(casperFFGLink).size() > (((numOfStakeholders / 3) * 2) + 1)) {
                justifiedBlocks.add(casperFFGLink.getToBeJustified());
                finalizedBlocks.add(casperFFGLink.getToBeFinalized());
                if (casperFFGLink.getToBeFinalized() instanceof BlockWithTx) {
                    finalizedTxs.addAll(((BlockWithTx<T>) casperFFGLink.getToBeFinalized()).getTxs());
                }
            }
        }
    }

    @Override
    protected void updateChain() {
        this.acceptedBlocks = this.localBlockTree.getAllAncestors(this.currentMainChainHead);
        if ((currentMainChainHead.getHeight() - checkpointSpace) > latestCheckpoint) {
            latestCheckpoint = currentMainChainHead.getHeight() - (currentMainChainHead.getHeight() % checkpointSpace);
            B toBeFinalizedBlock = justifiedBlocks.last();
            B toBeJustifiedBlock = localBlockTree.getAncestorOfHeight(currentMainChainHead, latestCheckpoint);
            CasperFFGLink<B> casperFFGLink = new CasperFFGLink<>(toBeFinalizedBlock, toBeJustifiedBlock);
            CasperFFGVote<B> casperFFGVote = new CasperFFGVote<>(this.blockchainNode, casperFFGLink);
            VoteMessage voteMessage = new VoteMessage(casperFFGVote);
            Packet packet = new Packet(this.blockchainNode, this.blockchainNode, voteMessage);
            this.blockchainNode.processIncomingPacket(packet);
        }
    }

    public int getNumOfJustifiedBlocks() {
        return this.justifiedBlocks.size();
    }

    @Override
    public boolean isBlockFinalized(B block) {
        return localBlockTree.getAllAncestors(this.finalizedBlocks.last()).contains(block);
    }

    @Override
    public boolean isTxFinalized(T tx) {
        return this.finalizedTxs.contains(tx);
    }

    public int getNumOfFinalizedBlocks() {
        return localBlockTree.getAllAncestors(this.finalizedBlocks.last()).size();
    }

    @Override
    public int getNumOfFinalizedTxs() {
        return this.finalizedTxs.size();
    }
}
