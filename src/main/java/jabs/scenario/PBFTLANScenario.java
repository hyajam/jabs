package jabs.scenario;

import jabs.data.Vote;
import jabs.data.pbft.PBFTCommitVote;
import jabs.data.pbft.PBFTPrePrepareVote;
import jabs.data.pbft.PBFTPrepareVote;
import jabs.event.PacketDeliveryEvent;
import jabs.message.Packet;
import jabs.message.VoteMessage;
import jabs.network.BlockFactory;
import jabs.network.PBFTLocalLANNetwork;
import jabs.node.nodes.pbft.PBFTNode;
import jabs.random.Random;

import static jabs.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;

public class PBFTLANScenario extends AbstractScenario {
    public PBFTLANScenario(int seed) {
        this.random = new Random(seed);
    }

    @Override
    public void createNetwork() {
        network = new PBFTLocalLANNetwork(random);
        network.populateNetwork(this.simulator, 4);

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
    protected void insertInitialEvents() {

    }

    @Override
    public boolean simulationStopCondition() {
        if (simulator.peekEvent() instanceof PacketDeliveryEvent) {
            if (((PacketDeliveryEvent) simulator.peekEvent()).packet.getMessage() instanceof VoteMessage) {
                Packet packet = ((PacketDeliveryEvent) simulator.peekEvent()).packet;
                Vote vote = ((VoteMessage) packet.getMessage()).getVote();

                String voteType = "";
                if (vote instanceof PBFTCommitVote) {
                    voteType = "COMMIT";
                } else if (vote instanceof PBFTPrepareVote) {
                    voteType = "PREPARE";
                } else if (vote instanceof PBFTPrePrepareVote) {
                    voteType = "PREPREPARE";
                }

                System.out.printf("Vote message delivery of type %s, Voter id: %d. from node %d to node %d\n", voteType,
                        vote.getVoter().nodeID, packet.getFrom().nodeID, packet.getTo().nodeID);
            }
        }
        return (simulator.getCurrentTime() > 10000);
    }

    @Override
    public void finishSimulation() {

    }
}
