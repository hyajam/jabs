package main.java.scenario;

import main.java.data.pbft.PBFTPrePrepareVote;
import main.java.message.VoteMessage;
import main.java.network.BlockFactory;
import main.java.network.PBFTLocalLANNetwork;
import main.java.node.nodes.pbft.PBFTNode;

import static main.java.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;

public class PBFTLANScenario extends AbstractScenario {
    @Override
    public void setupSimulation() {
        network = new PBFTLocalLANNetwork();

        network.getAllNodes().get(0).broadcastMessage(
                new VoteMessage(
                        new PBFTPrePrepareVote<>(network.getAllNodes().get(0),
                                BlockFactory.samplePBFTBlock(simulator,
                                        (PBFTNode) network.getAllNodes().get(0), PBFT_GENESIS_BLOCK)
                        )
                )
        );
    }

    @Override
    public boolean simulationStopCondition() {
        return (simulator.getCurrentTime() > 10000);
    }

    @Override
    public void outputResults() {

    }
}
