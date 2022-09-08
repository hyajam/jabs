package jabs.log;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;
import jabs.simulator.event.BlockConfirmationEvent;
import jabs.simulator.event.Event;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class BlockConfirmationLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public BlockConfirmationLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public BlockConfirmationLogger(Path path) throws IOException {
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
        return event instanceof BlockConfirmationEvent;
    }

    @Override
    protected boolean csvOutputConditionFinalPerNode() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "NodeID", "BlockHeight", "BlockHashCode", "BlockSize", "BlockCreationTime", "BlockCreator"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        Node node = ((BlockConfirmationEvent) event).getNode();
        Block block = ((BlockConfirmationEvent) event).getBlock();

        return new String[]{
                Double.toString(this.scenario.getSimulator().getSimulationTime()),
                Integer.toString(node.nodeID),
                Integer.toString(block.getHeight()),
                Integer.toString(block.hashCode()),
                Integer.toString(block.getSize()),
                Double.toString(block.getCreationTime()),
                Integer.toString(block.getCreator().nodeID)
        };
    }
}
