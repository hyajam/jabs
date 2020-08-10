package main.java.consensus;

import main.java.data.Block;
import main.java.data.Tx;
import main.java.blockchain.LocalBlockTree;
import main.java.data.Vote;
import main.java.data.casper.CasperFFGLink;
import main.java.data.casper.CasperFFGVote;
import main.java.message.VoteMessage;
import main.java.node.nodes.Node;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

public class CasperFFG<B extends Block<B>, T extends Tx<T>> extends GhostProtocol<B, T>
        implements VotingBasedConsensus<B, T> {
    private final HashMap<CasperFFGLink<B>, HashMap<Node, CasperFFGVote<B>>> votes = new HashMap<>();
    private final SortedSet<B> justifiedBlocks = new TreeSet<>();
    private final SortedSet<B> finalizedBlocks = new TreeSet<>();
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
            this.blockchainNode.broadcastMessage(voteMessage);
        }
    }

    public int getNumOfJustifiedBlocks() {
        return this.justifiedBlocks.size();
    }

    public int getNumOfFinalizedBlocks() {
        return this.finalizedBlocks.size();
    }
}
