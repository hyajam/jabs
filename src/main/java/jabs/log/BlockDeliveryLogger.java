package jabs.log;

import jabs.ledgerdata.Block;
import jabs.simulator.event.Event;
import jabs.simulator.event.PacketDeliveryEvent;
import jabs.network.message.DataMessage;
import jabs.network.message.Message;
import jabs.network.message.Packet;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class BlockDeliveryLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public BlockDeliveryLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public BlockDeliveryLogger(Path path) throws IOException {
        super(path);
    }

    @Override
    protected String csvStartingComment() {
        return String.format("Simulation name: %s      Number of nodes: %d      Network type: %s", scenario.getName(),
                this.scenario.getNetwork().getAllNodes().size(), this.scenario.getNetwork().getClass().getSimpleName());
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent(Event event) {
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent(Event event) {
        if (event instanceof PacketDeliveryEvent) {
            Message message = ((PacketDeliveryEvent) event).packet.getMessage();
            if (message instanceof DataMessage) {
                return (((DataMessage) message).getData() instanceof Block);
            }
        }
        return false;
    }

    @Override
    protected boolean csvOutputConditionFinalPerNode() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "DelayFromCreation", "BlockHeight", "BlockCreator", "BlockSize", "BlockHashCode", "Receiver", "Sender"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        Packet packet = ((PacketDeliveryEvent) event).packet;
        Block block = ((Block) ((DataMessage) packet.getMessage()).getData());

        return new String[]{
                Double.toString(this.scenario.getSimulator().getSimulationTime()),
                Double.toString(this.scenario.getSimulator().getSimulationTime() - block.getCreationTime()),
                Integer.toString(block.getHeight()),
                Integer.toString(block.getCreator().nodeID),
                Integer.toString(block.getSize()),
                Integer.toString(block.hashCode()),
                Integer.toString(packet.getTo().nodeID),
                Integer.toString(packet.getFrom().nodeID)
        };
    }
}
