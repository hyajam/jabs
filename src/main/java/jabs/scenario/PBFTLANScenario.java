package jabs.scenario;

import jabs.data.pbft.PBFTPrePrepareVote;
import jabs.message.VoteMessage;
import jabs.network.BlockFactory;
import jabs.network.PBFTLocalLANNetwork;
import jabs.node.nodes.pbft.PBFTNode1;

import static jabs.node.nodes.pbft.PBFTNode1.PBFT_GENESIS_BLOCK;

public class PBFTLANScenario extends AbstractScenario {
    @Override
    public void setupSimulation() {
        network = new PBFTLocalLANNetwork(random);

        network.getAllNodes().get(0).broadcastMessage(
                new VoteMessage(
                        new PBFTPrePrepareVote<>(network.getAllNodes().get(0),
                                BlockFactory.samplePBFTBlock(simulator, network.getRandom(),
                                        (PBFTNode1) network.getAllNodes().get(0), PBFT_GENESIS_BLOCK)
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
