package jabs.log;

import jabs.consensus.blockchain.LocalBlockTree;
import jabs.ledgerdata.Block;
import jabs.ledgerdata.Data;
import jabs.ledgerdata.SingleParentPoWBlock;
import jabs.network.message.DataMessage;
import jabs.network.message.Message;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.simulator.event.Event;
import jabs.simulator.event.PacketReceivingProcess;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public class BlockchainReorgLogger<B extends SingleParentPoWBlock<B>> extends AbstractCSVLogger {
    protected LocalBlockTree<B> NetworkViewBlockTree;
    private boolean newBlockReceived = false;
    private B previousHeadChain = null;
    private Node currentNode = null;

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
        B genesisBlock =
                (B) ((PeerBlockchainNode) this.scenario.getNetwork().getNode(0)).getConsensusAlgorithm()
                        .getLocalBlockTree().getGenesisBlock();
        this.NetworkViewBlockTree = new LocalBlockTree<>(genesisBlock);
        return String.format("Simulation name: %s      Number of nodes: %d      Network type: %s", scenario.getName(),
                this.scenario.getNetwork().getAllNodes().size(), this.scenario.getNetwork().getClass().getSimpleName());
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent(Event event) {
        if (event instanceof PacketReceivingProcess) {
            if (!((PacketReceivingProcess) event).isQueueEmpty()) {
                Message message = ((PacketReceivingProcess) event).peek().getMessage();
                Node node = ((PacketReceivingProcess) event).getNode();
                if (message instanceof DataMessage) {
                    Data data = ((DataMessage) message).getData();
                    if (data instanceof Block) {
                        NetworkViewBlockTree.add((B) data);
                        this.previousHeadChain = ((B) ((PeerBlockchainNode) node).getConsensusAlgorithm()
                                .getCanonicalChainHead());
                        newBlockReceived = true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean csvOutputConditionAfterEvent(Event event) {
        if (newBlockReceived) {
            newBlockReceived = false;
            this.currentNode = ((PacketReceivingProcess) event).getNode();
            B currentHeadChain = ((B) ((PeerBlockchainNode) this.currentNode).getConsensusAlgorithm()
                    .getCanonicalChainHead());
            B ancestor = NetworkViewBlockTree.getAncestorOfHeight(currentHeadChain, this.previousHeadChain.getHeight());
            return ancestor != this.previousHeadChain;
        }
        return false;
    }

    @Override
    protected boolean csvOutputConditionFinalPerNode() {
        return false;
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[]{"Time", "NodeID", "BlockHeight", "BlockCreationTime", "BlockCreator", "ReorgLength"};
    }

    @Override
    protected String[] csvEventOutput(Event event) {
        B nodeChainHead = ((B) ((PeerBlockchainNode) this.currentNode).getConsensusAlgorithm().getCanonicalChainHead());
        B commonAncestor = NetworkViewBlockTree.getCommonAncestor(nodeChainHead, this.previousHeadChain);
        int reorgLength = nodeChainHead.getHeight() - commonAncestor.getHeight();

        return new String[]{
                Double.toString(this.scenario.getSimulator().getSimulationTime()),
                Integer.toString(this.currentNode.nodeID),
                Integer.toString(nodeChainHead.getHeight()),
                Double.toString(nodeChainHead.getCreationTime()),
                Integer.toString(nodeChainHead.getCreator().nodeID),
                Integer.toString(reorgLength)
        };
    }
}
