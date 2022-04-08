package jabs.log;

import jabs.data.Vote;
import jabs.data.pbft.PBFTCommitVote;
import jabs.data.pbft.PBFTPrePrepareVote;
import jabs.data.pbft.PBFTPrepareVote;
import jabs.event.PacketDeliveryEvent;
import jabs.message.Packet;
import jabs.message.VoteMessage;
import jabs.scenario.AbstractScenario;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class PBFTCSVLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public PBFTCSVLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public PBFTCSVLogger(Path path) throws IOException {
        super(path);
    }

    @Override
    protected String csvStartingComment() {
        return String.format("PBFT Simulation with %d nodes on %s network", this.scenario.getNetwork().getAllNodes().size(), this.scenario.getNetwork().getClass().getSimpleName());
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent() {
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent() {
        if (this.scenario.getSimulator().peekEvent() instanceof PacketDeliveryEvent) {
            return ((PacketDeliveryEvent) this.scenario.getSimulator().peekEvent()).packet.getMessage() instanceof VoteMessage;
        }
        return false;
    }

    @Override
    protected boolean csvOutputConditionFinal() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Vote message type", "Voter ID", "From Node", "To Node"};
    }

    @Override
    protected String[] csvLineOutput() {
        Packet packet = ((PacketDeliveryEvent) this.scenario.getSimulator().peekEvent()).packet;
        Vote vote = ((VoteMessage) packet.getMessage()).getVote();

        String voteType = "";
        if (vote instanceof PBFTCommitVote) {
            voteType = "COMMIT";
        } else if (vote instanceof PBFTPrepareVote) {
            voteType = "PREPARE";
        } else if (vote instanceof PBFTPrePrepareVote) {
            voteType = "PREPREPARE";
        }

        return new String[]{Double.toString(this.scenario.getSimulator().getCurrentTime()), voteType,
                Integer.toString(vote.getVoter().nodeID), Integer.toString(packet.getFrom().nodeID),
                Integer.toString(packet.getTo().nodeID)};
    }
}
