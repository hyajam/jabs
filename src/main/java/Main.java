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
import main.java.scenario.AbstractScenario;
import main.java.scenario.NormalEthereumNetworkScenario;
import main.java.simulator.AbstractSimulator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import static main.java.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;
import static main.java.simulator.AbstractSimulator.*;

public class Main {
    public static RandomGenerator random = new MersenneTwister(100);

    public static void main(String[] args) {
        AbstractScenario scenario = new NormalEthereumNetworkScenario();
        scenario.simulationSetup();

        while (scenario.simulationStopCondition()) {
            executeNextEvent();
        }

        scenario.postSimulation();
    }
}
