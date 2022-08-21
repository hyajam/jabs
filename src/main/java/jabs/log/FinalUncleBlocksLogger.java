package jabs.log;

import jabs.consensus.algorithm.ConsensusAlgorithm;
import jabs.consensus.blockchain.LocalBlockTree;
import jabs.ledgerdata.Block;
import jabs.ledgerdata.Data;
import jabs.ledgerdata.SingleParentPoWBlock;
import jabs.network.message.DataMessage;
import jabs.network.message.Message;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.network.node.nodes.PeerDLTNode;
import jabs.simulator.event.Event;
import jabs.simulator.event.PacketReceivingProcess;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

/**
 *
 */
public class FinalUncleBlocksLogger extends AbstractCSVLogger {
    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public FinalUncleBlocksLogger(Writer writer) {
        super(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public FinalUncleBlocksLogger(Path path) throws IOException {
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
        return false;
    }

    @Override
    protected boolean csvOutputConditionFinalPerNode() {
        return true;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"NodeID", "CanonicalChainLength", "NumUncles"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        return new String[0];
    }

    @Override
    protected String[] csvNodeOutput(Node node) {
        int canonicalHeadLen =
                ((PeerBlockchainNode) node).getConsensusAlgorithm().getCanonicalChainHead().getHeight();
        int totalBlocks = ((PeerBlockchainNode) node).getConsensusAlgorithm().getLocalBlockTree().size();
        int numUncles = totalBlocks - canonicalHeadLen - 1; // The genesis block shall not be counted

        return new String[]{
                Integer.toString(node.nodeID),
                Integer.toString(canonicalHeadLen),
                Integer.toString(numUncles),
        };
    }
}
