package jabs.scenario;

import jabs.ledgerdata.pbft.PBFTPrePrepareVote;
import jabs.log.AbstractLogger;
import jabs.network.message.VoteMessage;
import jabs.network.networks.BlockFactory;
import jabs.network.networks.PBFTLocalLANNetwork;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.pbft.PBFTNode;

import static jabs.network.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;

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
        Node node = (Node) network.getAllNodes().get(0);
        node.broadcastMessage(
                new VoteMessage(
                        new PBFTPrePrepareVote<>(node,
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
