package main.java;

import main.java.consensus.CasperFFG;
import main.java.consensus.PBFT;
import main.java.data.pbft.PBFTBlock;
import main.java.data.pbft.PBFTPrePrepareVote;
import main.java.event.RandomNodeBlockGeneratorProcess;
import main.java.event.RandomNodeTxGeneratorProcess;
import main.java.message.VoteMessage;
import main.java.network.BlockFactory;
import main.java.network.Network;
import main.java.network.NetworkBuilder;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.node.nodes.ethereum.EthereumNode;
import main.java.node.nodes.pbft.PBFTNode;
import main.java.simulator.AbstractSimulator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import static main.java.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;
import static main.java.simulator.AbstractSimulator.*;

public class Main {
    public static RandomGenerator random = new MersenneTwister(123);
    public static int numAllStakeHolders = 110;

    public static void main(String[] args) {
        NetworkBuilder.buildSampleEthereumNetwork(100, 10);

//        NetworkBuilder.buildSamplePBFTNetwork(4);
//
//        Network.getAllNodes().get(0).broadcastMessage(
//                new VoteMessage(
//                        new PBFTPrePrepareVote<>(Network.getAllNodes().get(0),
//                                BlockFactory.samplePBFTBlock(
//                                        (PBFTNode) Network.getAllNodes().get(0), PBFT_GENESIS_BLOCK)
//                        )
//                )
//        );

//        AbstractSimulator.putEvent(new RandomNodeTxGeneratorProcess(500, 2000), AbstractSimulator.getCurrentTime());
        AbstractSimulator.putEvent(new RandomNodeBlockGeneratorProcess(1000, 1000), AbstractSimulator.getCurrentTime());

        long lastSecond = 0;
        while (thereIsMoreEvents()) {
            if ((AbstractSimulator.getCurrentTime() - lastSecond) >= 1000){
                System.out.println(AbstractSimulator.getCurrentTime());
                lastSecond = AbstractSimulator.getCurrentTime();
            }
            executeNextEvent();
        }

        for (Node node:Network.getAllNodes()) {
            System.out.println( ((CasperFFG) ((BlockchainNode) node).getConsensusAlgorithm()).getNumOfFinalizedBlocks() );
        }
    }
}
