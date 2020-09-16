package main.java.scenario;

import main.java.data.pbft.PBFTPrePrepareVote;
import main.java.message.VoteMessage;
import main.java.network.BlockFactory;
import main.java.network.Network;
import main.java.network.NetworkBuilder;
import main.java.node.nodes.pbft.PBFTNode;
import main.java.simulator.Simulator;

import static main.java.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;

public class PBFTLANScenario extends AbstractScenario {
    @Override
    public void setupSimulation() {
        NetworkBuilder.buildSamplePBFTNetwork(4);

        Network.getAllNodes().get(0).broadcastMessage(
                new VoteMessage(
                        new PBFTPrePrepareVote<>(Network.getAllNodes().get(0),
                                BlockFactory.samplePBFTBlock(
                                        (PBFTNode) Network.getAllNodes().get(0), PBFT_GENESIS_BLOCK)
                        )
                )
        );
    }

    @Override
    public boolean simulationStopCondition() {
        return (Simulator.getCurrentTime() > 10000);
    }

    @Override
    public void outputResults() {

    }
}
