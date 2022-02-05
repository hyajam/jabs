package jabs.scenario;

import de.siegmar.fastcsv.writer.CsvWriter;
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

import static jabs.node.nodes.pbft.PBFTNode.PBFT_GENESIS_BLOCK;

public class PBFTLANScenario extends AbstractScenario {
    protected int numNodes;
    protected long simulationStopTime;

    public PBFTLANScenario(long seed, CsvWriter outCSV, int numNodes, long simulationStopTime) {
        super(seed, outCSV);
        this.numNodes = numNodes;
        this.simulationStopTime = simulationStopTime;
    }

    @Override
    public void createNetwork() {
        network = new PBFTLocalLANNetwork(random);
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

    @Override
    public void finishSimulation() {
        System.err.println("Simulation finished");
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent() {
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent() {
        if (simulator.peekEvent() instanceof PacketDeliveryEvent) {
            return ((PacketDeliveryEvent) simulator.peekEvent()).packet.getMessage() instanceof VoteMessage;
        }
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Vote message type", "Voter ID", "From Node", "To Node"};
    }

    @Override
    protected String[] csvLineOutput() {
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

        return new String[]{Long.toString(this.simulator.getCurrentTime()), voteType,
                Integer.toString(vote.getVoter().nodeID), Integer.toString(packet.getFrom().nodeID),
                Integer.toString(packet.getTo().nodeID)};
    }
}
