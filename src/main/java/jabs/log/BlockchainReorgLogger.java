package jabs.log;

import jabs.ledgerdata.Block;
import jabs.network.node.nodes.Node;
import jabs.simulator.event.BlockConfirmationEvent;
import jabs.simulator.event.BlockchainReorgEvent;
import jabs.simulator.event.Event;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class BlockchainReorgLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public BlockchainReorgLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public BlockchainReorgLogger(Path path) throws IOException {
        super(path);
    }

    @Override
    protected String csvStartingComment() {
        return String.format("Simulation name: %s      Number of nodes: %d      Network type: %s", scenario.getName(),
                this.scenario.getNetwork().getAllNodes().size(), this.scenario.getNetwork().getClass().getSimpleName());
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent() {
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent() {
        Event event = this.scenario.getSimulator().peekEvent();
        return event instanceof BlockchainReorgEvent;
    }

    @Override
    protected boolean csvOutputConditionFinal() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "NodeID", "BlockHeight", "BlockCreationTime", "BlockCreator", "ReorgLength"};
    }

    @Override
    protected String[] csvLineOutput() {
        Event event = this.scenario.getSimulator().peekEvent();
        Node node = ((BlockchainReorgEvent) event).getNode();
        Block block = ((BlockchainReorgEvent) event).getBlock();
        int reorgLength = ((BlockchainReorgEvent) event).getReorgLength();

        return new String[]{
                Double.toString(this.scenario.getSimulator().getCurrentTime()),
                Integer.toString(node.nodeID),
                Integer.toString(block.getHeight()),
                Double.toString(block.getCreationTime()),
                Integer.toString(block.getCreator().nodeID),
                Integer.toString(reorgLength)
        };
    }
}
