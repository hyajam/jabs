package jabs.log;

import jabs.data.Data;
import jabs.data.Vote;
import jabs.data.bitcoin.BitcoinBlock;
import jabs.data.pbft.PBFTCommitVote;
import jabs.data.pbft.PBFTPrePrepareVote;
import jabs.data.pbft.PBFTPrepareVote;
import jabs.event.BlockGenerationEvent;
import jabs.event.Event;
import jabs.event.PacketDeliveryEvent;
import jabs.message.DataMessage;
import jabs.message.Message;
import jabs.message.Packet;
import jabs.message.VoteMessage;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class BitcoinBlockLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public BitcoinBlockLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public BitcoinBlockLogger(Path path) throws IOException {
        super(path);
    }

    @Override
    protected String csvStartingComment() {
        return String.format("Bitcoin Global Network Simulation with %d nodes on %s network",
                this.scenario.getNetwork().getAllNodes().size(), this.scenario.getNetwork().getClass().getSimpleName());
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent() {
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent() {
        Event event = this.scenario.getSimulator().peekEvent();
        if (event instanceof PacketDeliveryEvent) {
            Message message = ((PacketDeliveryEvent) event).packet.getMessage();
            if (message instanceof DataMessage) {
                return (((DataMessage) message).getData() instanceof BitcoinBlock);
            }
        }
        return false;
    }

    @Override
    protected boolean csvOutputConditionFinal() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "BlockHeight", "BlockCreator", "BlockSize", "Sender", "Receiver"};
    }

    @Override
    protected String[] csvLineOutput() {
        Event event = this.scenario.getSimulator().peekEvent();
        Packet packet = ((PacketDeliveryEvent) event).packet;
        BitcoinBlock block = ((BitcoinBlock) ((DataMessage) packet.getMessage()).getData());

        return new String[]{
                Double.toString(this.scenario.getSimulator().getCurrentTime()),
                Integer.toString(block.getHeight()),
                Integer.toString(block.getCreator().nodeID), Integer.toString(block.getSize()),
                Integer.toString(packet.getFrom().nodeID), Integer.toString(packet.getTo().nodeID)
        };
    }
}
