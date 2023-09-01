package jabs.consensus.algorithm;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.ledgerdata.BlockFactory;
import jabs.ledgerdata.SingleParentBlock;
import jabs.ledgerdata.Tx;
import jabs.ledgerdata.Query;
import jabs.ledgerdata.snow.*;
import jabs.network.message.QueryMessage;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.snow.SnowNode;
import jabs.simulator.randengine.RandomnessEngine;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * File: Snow.java
 * Description: Implements SnowBall protocol for JABS blockchain simulator.
 * Author: Siamak Abdi
 * Date: August 22, 2023
 */

public class Snow<B extends SingleParentBlock<B>, T extends Tx<T>> extends AbstractChainBasedConsensus<B, T>
        implements QueryingBasedConsensus<B, T>, DeterministicFinalityConsensus<B, T> {

    static {
        try {
            File file = new File("output/snow-events-log.txt");
            if (file.exists()) {
                file.delete();
            }
            writer = new PrintWriter(new FileWriter(file, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //*-------------------------------------------------------------------------------------------------------
    // settings for the Snow protocols
    public static int k = 2; // sample size
    private int alpha = k/2; // quorum size or threshold
    private int beta = 5; // conviction threshold
    private double mean = 0.5; // Mean of the normal distribution
    private double stdDev = 0.1; // Standard deviation of the normal distribution
    //*-------------------------------------------------------------------------------------------------------
    private static PrintWriter writer;
    private final int numAllParticipants;
    private static int numReadyNodes;
    private static boolean readyTransfer;
    private final HashMap<B, HashMap<Node, Query>> commitQueries = new HashMap<>();
    private final static HashSet<Node> committedNodes = new HashSet<>();
    public static int numConsensus;
    private int numReceivedSamples;
    private int conviction;
    public boolean consensus;
    private SnowPhase snowPhase = SnowPhase.PREPARING;
    private SnowMode snowMode = SnowMode.NORMAL;
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

    public enum SnowPhase {
        PREPARING,
        COMMITTING
    }
    private enum SnowMode{
        NORMAL,
        TRANSFERRING
    }

    public Snow(LocalBlockTree<B> localBlockTree, int numAllParticipants) {
        super(localBlockTree);
        this.numAllParticipants = numAllParticipants;
        this.currentMainChainHead = localBlockTree.getGenesisBlock();
    }

    public void newIncomingQuery(Query query) {
        if (query instanceof SnowBlockQuery) {
            SnowBlockQuery<B> blockQuery = (SnowBlockQuery<B>) query;
            B block = blockQuery.getBlock();
            SnowNode peer = (SnowNode) this.peerBlockchainNode;
            if(peer.currentBlock.getHeight() < block.getHeight()){ // If the node has not yet voted for the new block
                double sample = generateNormalSample(this.peerBlockchainNode.getNetwork().getRandom(), mean, stdDev); // decides whether to vote for the new block or not
                peer.currentBlock = sample >= 0.5 ?  (SnowBlock) block : peer.currentBlock;
            }
            SnowNode inquirer = (SnowNode) blockQuery.getInquirer();
            switch (blockQuery.getQueryType()) {
                case PREPARE :
                    if (!this.localBlockTree.contains(block)) {
                        this.localBlockTree.add(block);
                    }
                    if (this.localBlockTree.getLocalBlock(block).isConnectedToGenesis) {
                        this.snowPhase = SnowPhase.COMMITTING;
                        this.peerBlockchainNode.respondQuery(
                                new QueryMessage(
                                        new SnowCommitQuery<>(this.peerBlockchainNode, peer.currentBlock)
                                ), inquirer
                        );
                    }
                    break;
                case COMMIT:
                    checkQueries(blockQuery, (B) peer.currentBlock, this.commitQueries, SnowPhase.PREPARING);
                    break;
            }
        }
    }

    private void checkQueries(SnowBlockQuery<B> query, B block, HashMap<B, HashMap<Node, Query>> queries, Snow.SnowPhase nextStep) {
        this.snowPhase = nextStep;
        this.numReceivedSamples++;
        List<Node> sampledNeighbors = sampleNeighbors(this.peerBlockchainNode.getNetwork().getRandom(), k);
        if (!queries.containsKey(block)) { // the first query reply received for the block
            queries.put(block, new HashMap<>());
        }
        queries.get(block).put(query.getInquirer(), query);
        if (numReceivedSamples == k) { // if the node has received all the replies
            this.numReceivedSamples = 0;
            if(this.snowMode == SnowMode.TRANSFERRING){
                if(!readyTransfer){
                    this.snowMode = SnowMode.NORMAL;
                }
                queries.clear();
                continueSampling(nextStep, sampledNeighbors, block); // Just to participate in the process
            }else if(this.snowMode == SnowMode.NORMAL){
                if(committedNodes.contains(this.peerBlockchainNode)){ // if the node has reached consensus (beta threshold)
                    queries.clear();
                    if(!this.consensus){
                        checkConsensus(block);
                    }
                    if(!readyTransfer){
                        continueSampling(nextStep, sampledNeighbors, block); // Just to participate in the process
                    }else {
                        startGenerateNewBlock(nextStep, sampledNeighbors, block);
                    }
                } else if ((queries.get(block).size() > alpha) && (block.getHeight()>this.currentMainChainHead.getHeight())) { // If a successful query for the block reaches the alpha value
                    this.conviction++;
                    queries.clear();
                    if((this.conviction==beta)){ // if the node's conviction value reaches the beta value (local consensus)
                        committedNodes.add(this.peerBlockchainNode);
                        this.conviction = 0;
                        if(!this.consensus){
                            checkConsensus(block);
                        }
                    }
                    continueSampling(nextStep, sampledNeighbors, block); // Just to participate in the process
                } else {
                    queries.clear();
                    this.conviction = 0;
                    continueSampling(nextStep, sampledNeighbors, block); // Just to participate in the process
                }
            }
        }
    }

    private void startGenerateNewBlock(Snow.SnowPhase nextStep, List<Node> sampledNeighbors, B block) {
        //System.out.println("node "+this.peerBlockchainNode.nodeID+" started"+ " at "+this.peerBlockchainNode.getSimulator().getSimulationTime());
        this.conviction = 0;
        this.snowMode = SnowMode.TRANSFERRING;
        committedNodes.remove(this.peerBlockchainNode);
        this.consensus = false;
        if(committedNodes.size()==0){
            numReadyNodes = 0;
            readyTransfer = false;
            //System.out.println("All transferred!");
        }
        switch (nextStep) {
            case PREPARING:
                if(this.peerBlockchainNode.nodeID == this.getCurrentPrimaryNumber()) { // The next node to start proposing a new block
                    SnowNode snowNode = (SnowNode) this.peerBlockchainNode;
                    SnowBlock newBlock = BlockFactory.sampleSnowBlock(this.peerBlockchainNode.getSimulator(), this.peerBlockchainNode.getNetwork().getRandom(),
                            snowNode, (SnowBlock) block);
                    snowNode.currentBlock = newBlock;
                    for (Node destination : sampledNeighbors) {
                        snowNode.query(
                                new QueryMessage(
                                        new SnowPrepareQuery<>(snowNode, newBlock)
                                ), destination
                        );
                    }
                }else {
                    SnowNode snowNode = (SnowNode) this.peerBlockchainNode;
                    snowNode.currentBlock = (SnowBlock) block;
                    for(Node destination:sampledNeighbors){
                        snowNode.query(
                                new QueryMessage(
                                        new SnowPrepareQuery<>(snowNode, block)
                                ), destination
                        );
                    }
                }
                break;
        }
    }

    private void continueSampling(Snow.SnowPhase nextStep, List<Node> sampledNeighbors, B block) {
        switch (nextStep) {
            case PREPARING:
                for(Node destination:sampledNeighbors){
                    this.peerBlockchainNode.query(
                            new QueryMessage(
                                    new SnowPrepareQuery<>(this.peerBlockchainNode, block)
                            ), destination
                    );
                }
                break;
        }
    }

    private void checkConsensus(B block) {
        if(committedNodes.size() == getNumAllParticipants()){
            //System.out.println("node "+this.peerBlockchainNode.nodeID+" is ready"+ " at "+this.peerBlockchainNode.getSimulator().getSimulationTime());
            this.consensus=true;
            numReadyNodes++;
            if(numReadyNodes==getNumAllParticipants()){
                readyTransfer = true;
            }
            this.currentMainChainHead = block; // accepts the block
            updateChain();
            writer.println("Consensus occurred in node " + this.peerBlockchainNode.getNodeID() + " for the block " + block.getHeight() + " at " + this.peerBlockchainNode.getSimulator().getSimulationTime());
            writer.flush();
        }
    }

    private List<Node> sampleNeighbors(RandomnessEngine randomnessEngine, int k) {
        List<Node> neighbors = new ArrayList<>();
        neighbors.addAll(this.peerBlockchainNode.getP2pConnections().getNeighbors());
        neighbors.remove(this.peerBlockchainNode);
        List<Node> sampledNodes = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            int randomIndex = randomnessEngine.sampleInt(neighbors.size());
            sampledNodes.add(neighbors.get(randomIndex));
            neighbors.remove(randomIndex);
        }
        return sampledNodes;
    }
    private static double generateNormalSample(RandomnessEngine random, double mean, double stdDev) {
        return mean + stdDev * random.nextGaussian();
    }

    @Override
    public void newIncomingBlock(B block) {

    }

    /**
     * @param block
     * @return
     */
    @Override
    public boolean isBlockConfirmed(B block) {
        return false;
    }

    /**
     * @param block
     * @return
     */
    @Override
    public boolean isBlockValid(B block) {
        return false;
    }

    public int getNumAllParticipants() {
        return this.numAllParticipants;
    }

    public Snow.SnowPhase getSnowPhase() {
        return this.snowPhase;
    }

    public int getCurrentPrimaryNumber() {
        return (this.currentMainChainHead.getHeight() % this.numAllParticipants);
    }

    @Override
    protected void updateChain() {
        this.confirmedBlocks.add(this.currentMainChainHead);
        numConsensus = this.confirmedBlocks.size();
    }

}
