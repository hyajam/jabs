package jabs.scenario;

import jabs.data.pbft.PBFTPrePrepareVote;
import jabs.log.AbstractLogger;
import jabs.message.VoteMessage;
import jabs.network.BlockFactory;
import jabs.network.PBFTLocalLANNetwork;
import jabs.node.nodes.pbft.PBFTNode;

import static jabs.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;

public class PBFTLANScenario extends AbstractScenario {
    protected int numNodes;
    protected double simulationStopTime;

    public PBFTLANScenario(String name, long seed, AbstractLogger logger, int numNodes, double simulationStopTime) {
        super(name, seed, logger);
        this.numNodes = numNodes;
        this.simulationStopTime = simulationStopTime;
    }

    @Override
    public void createNetwork() {
        network = new PBFTLocalLANNetwork(randomnessEngine);
        network.populateNetwork(this.simulator, this.numNodes);
    }

    @Override
    protected void insertInitialEvents() {
        network.getAllNodes().get(0).broadcastMessage(
                new VoteMessage(
                        new PBFTPrePrepareVote<>(network.getAllNodes().get(0),
                                BlockFactory.samplePBFTBlock(simulator, network.getRandom(),
                                        (PBFTNode) network.getAllNodes().get(0), PBFT_GENESIS_BLOCK)
                        )
                )
        );
    }

    @Override
    public boolean simulationStopCondition() {
        return (simulator.getCurrentTime() > this.simulationStopTime);
    }
}
