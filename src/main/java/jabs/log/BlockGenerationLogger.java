package jabs.log;

import jabs.network.node.nodes.MinerNode;
import jabs.network.node.nodes.Node;
import jabs.simulator.event.BlockMiningProcess;
import jabs.simulator.event.Event;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class BlockGenerationLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public BlockGenerationLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public BlockGenerationLogger(Path path) throws IOException {
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
        return event instanceof BlockMiningProcess;
    }

    @Override
    protected boolean csvOutputConditionFinalPerNode() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "NodeID"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        MinerNode miner = ((BlockMiningProcess) event).getMiner();
        Node node = ((Node) miner);

        return new String[]{
                Double.toString(this.scenario.getSimulator().getCurrentTime()),
                Double.toString(node.nodeID),
        };
    }
}
