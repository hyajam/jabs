package jabs.consensus.config;

import jabs.consensus.algorithm.AbstractChainBasedConsensus;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;
import jabs.simulator.event.BlockConfirmationEvent;

import java.util.List;
import java.lang.Math;

public class RoundRobinConsensus<B extends SingleParentBlock<B>, T extends Tx<T>>
        extends AbstractChainBasedConsensus<B, T> {
    private int longestChainLen = 0;
    private final double averageBlockMiningInterval;
    private final int confirmationDepth;

    List<MinerNode> allMiners = ((GlobalProofOfWorkNetwork) this.peerDLTNode.getNetwork()).getAllMiners();
    private final int numOfMiners = allMiners.size();
    private final double miningDiversity = 0.75;
    private final int spacingMinus1 = (int) ((Math.ceil(numOfMiners * miningDiversity)) - 1);

    public RoundRobinConsensus(LocalBlockTree<B> localBlockTree, NakamotoConsensusConfig nakamotoConsensusConfig) {
        super(localBlockTree);
        this.averageBlockMiningInterval = nakamotoConsensusConfig.averageBlockMiningInterval();
        this.confirmationDepth = nakamotoConsensusConfig.getConfirmationDepth();
        this.currentMainChainHead = localBlockTree.getGenesisBlock();
    }

    @Override
    public void newIncomingBlock(B block) {
        int currentId = block.getCreator().getNodeID();
        int currentHeight = block.getHeight();

        boolean flag = false;
        for(int i = 0; i < spacingMinus1 - 1; i++) {
            B parentIBlock = localBlockTree.getAncestorOfHeight(block,currentHeight - (i + 1));
            if(parentIBlock.getCreator().getNodeID() == currentId) {
                flag = true;
            }
        }

        if (!flag) {
            this.longestChainLen = block.getHeight();
            this.currentMainChainHead = block;
            this.updateChain();
        }
    }

    @Override
    protected void updateChain() {
        if (currentMainChainHead.getHeight() > confirmationDepth) {
            int heightOfConfirmedBlocks = currentMainChainHead.getHeight() - confirmationDepth;
            B highestConfirmedBlock =  localBlockTree.getAncestorOfHeight(currentMainChainHead, heightOfConfirmedBlocks);
            this.confirmedBlocks = this.localBlockTree.getAllAncestors(highestConfirmedBlock);
            Simulator simulator = this.peerDLTNode.getSimulator();
            double currentTime = simulator.getSimulationTime();
            simulator.putEvent(
                    new BlockConfirmationEvent(currentTime, this.peerDLTNode, highestConfirmedBlock),
                    0);
        }
    }

    public double getAverageBlockMiningInterval() {
        return averageBlockMiningInterval;
    }

    @Override
    public boolean isBlockValid(B block) {
        List<Integer> permittedMiners = null;
        int currentHeight = currentMainChainHead.getHeight() + 1;
        int firstMinerID = currentHeight % numOfMiners;
        if(firstMinerID == 0) {
            firstMinerID = numOfMiners;
        }
        int upperBound = firstMinerID + numOfMiners - spacingMinus1-1;
        for(int i = firstMinerID; i <= upperBound; i++) {
            if((upperBound) > numOfMiners) {
                int temp = upperBound % numOfMiners;
                permittedMiners.add(temp);
            }
            else {
                permittedMiners.add(i);
            }
        }
        //if(permittedMiners.contains(this.peerDLTNode.getNodeID())) {return  true;}
        if(permittedMiners.contains(block.getCreator().getNodeID())) {return  true;}
        else { return false;}
    }
}
