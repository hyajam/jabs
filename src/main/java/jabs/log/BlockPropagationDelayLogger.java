package jabs.log;

import jabs.ledgerdata.Block;
import jabs.network.message.DataMessage;
import jabs.network.message.Message;
import jabs.network.message.Packet;
import jabs.network.node.nodes.Node;
import jabs.simulator.event.Event;
import jabs.simulator.event.PacketDeliveryEvent;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;

public class BlockPropagationDelayLogger extends AbstractCSVLogger {
    private final double shareOfNodesReceivedBlock;
    private final HashMap<Block, HashSet<Node>> ReceivedBy = new HashMap<>();

    /**
     * creates an abstract CSV logger
     *
     * @param writer                    this is output CSV of the logger
     * @param shareOfNodesReceivedBlock
     */
    public BlockPropagationDelayLogger(Writer writer, double shareOfNodesReceivedBlock) {
        super(writer);
        this.shareOfNodesReceivedBlock = shareOfNodesReceivedBlock;
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public BlockPropagationDelayLogger(Path path, double shareOfNodesReceivedBlock) throws IOException {
        super(path);
        this.shareOfNodesReceivedBlock = shareOfNodesReceivedBlock;
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
            Packet packet = ((PacketDeliveryEvent) event).packet;
            Message message = packet.getMessage();
            if (message instanceof DataMessage) {
                if (((DataMessage) message).getData() instanceof Block) {
                    Block block = ((Block) ((DataMessage) message).getData());
                    Node Receiver = packet.getTo();
                    if (ReceivedBy.containsKey(block)) {
                        ReceivedBy.get(block).add(Receiver);
                    } else {
                        ReceivedBy.put(block, new HashSet<>());
                        ReceivedBy.get(block).add(Receiver);
                    }
                    int exactNumber = (int)(this.scenario.getNetwork().getAllNodes().size() * shareOfNodesReceivedBlock);
                    return (ReceivedBy.get(block).size() == exactNumber);
                }
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
        return new String[]{"Time", "PropagationDelay", "BlockHashCode", "BlockHeight", "BlockCreator", "BlockSize"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        Packet packet = ((PacketDeliveryEvent) event).packet;
        Block block = ((Block) ((DataMessage) packet.getMessage()).getData());

        return new String[]{
                Double.toString(this.scenario.getSimulator().getSimulationTime()),
                Double.toString(this.scenario.getSimulator().getSimulationTime() - block.getCreationTime()),
                Integer.toString(block.hashCode()),
                Integer.toString(block.getHeight()),
                Integer.toString(block.getCreator().nodeID),
                Integer.toString(block.getSize()),
        };
    }
}
