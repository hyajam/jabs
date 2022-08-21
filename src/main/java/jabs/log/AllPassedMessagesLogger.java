package jabs.log;

import jabs.simulator.event.Event;
import jabs.simulator.event.PacketDeliveryEvent;
import jabs.network.message.Message;
import jabs.network.message.Packet;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class AllPassedMessagesLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public AllPassedMessagesLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public AllPassedMessagesLogger(Path path) throws IOException {
        super(path);
    }

    @Override
    protected String csvStartingComment() {
        return String.format("Bitcoin Global Network Simulation with %d nodes on %s network",
                this.scenario.getNetwork().getAllNodes().size(), this.scenario.getNetwork().getClass().getName());
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent(Event event) {
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent(Event event) {
        return (event instanceof PacketDeliveryEvent);
    }

    @Override
    protected boolean csvOutputConditionFinalPerNode() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "MessageType", "Sender", "Receiver", "Size"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        Packet packet = ((PacketDeliveryEvent) event).packet;
        Message message = packet.getMessage();

        return new String[]{
                Double.toString(this.scenario.getSimulator().getCurrentTime()),
                message.getClass().getSimpleName(),
                Integer.toString(packet.getFrom().nodeID),
                Integer.toString(packet.getTo().nodeID),
                Integer.toString(packet.getSize())
        };
    }
}
