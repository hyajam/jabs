package jabs.scenario;

import de.siegmar.fastcsv.writer.CsvWriter;
import jabs.consensus.CasperFFG;
import jabs.consensus.DeterministicFinalityConsensus;
import jabs.event.PacketDeliveryEvent;
import jabs.message.VoteMessage;
import jabs.network.BlockchainNetwork;
import jabs.network.CasperFFGGlobalBlockchainNetwork;
import jabs.network.GlobalBlockchainNetwork;
import jabs.node.nodes.BlockchainNode;
import jabs.node.nodes.Node;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static jabs.event.EventFactory.createBlockGenerationEvents;
import static jabs.event.EventFactory.createTxGenerationEvents;

public class EthereumCasperNetworkScenario extends AbstractScenario {
    private final int numOfMiners;
    private final int numOfNonMiners;
    private final int checkpointSpace;
    private final long simulationStopTime;
    private final double txGenerationRate;
    private final double blockGenerationRate;

    long simulationTime = 0;
    long totalVoteMassageTraffic = 0;
    DescriptiveStatistics blockFinalizationTimes = new DescriptiveStatistics();

    public EthereumCasperNetworkScenario(long seed, CsvWriter outCSV, int numOfMiners, int numOfNonMiners, int checkpointSpace,
                                         long simulationStopTime, double txGenerationRate, double blockGenerationRate) {
        super(seed, outCSV);
        this.numOfMiners = numOfMiners;
        this.numOfNonMiners = numOfNonMiners;
        this.checkpointSpace = checkpointSpace;
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    public void createNetwork() {
        this.network = new CasperFFGGlobalBlockchainNetwork(random, checkpointSpace);
        ((GlobalBlockchainNetwork) network).populateNetwork(simulator, numOfMiners, numOfNonMiners);

        for (Node node:network.getAllNodes()) {
            ((CasperFFG) ((BlockchainNode) node).getConsensusAlgorithm()).enableFinalizationTimeRecords(blockFinalizationTimes);
        }
    }

    @Override
    protected void insertInitialEvents() {
        createTxGenerationEvents(simulator, random, network, ((int) (simulationStopTime*txGenerationRate)), (long)(1000/txGenerationRate));
        createBlockGenerationEvents(simulator, random, (BlockchainNetwork) network, ((int) (simulationStopTime*blockGenerationRate)), (long)(1000/blockGenerationRate));
    }

    @Override
    public boolean simulationStopCondition() {
        if (simulator.peekEvent() instanceof PacketDeliveryEvent) {
            if (((PacketDeliveryEvent) simulator.peekEvent()).packet.getMessage() instanceof VoteMessage) {
                totalVoteMassageTraffic += ((PacketDeliveryEvent) simulator.peekEvent()).packet.getSize();
            }
        }
        return (simulator.getCurrentTime() > simulationStopTime*1000);
    }

    @Override
    public void finishSimulation() {
        System.out.print("*** Ethereum CasperFFG Scenario ***\n");
        System.out.print("** Settings **\n");
        System.out.print("Seed value:\n");
        System.out.printf("Total Number of Nodes: %s\n", numOfMiners+numOfNonMiners);
        System.out.printf("Total Number of Miners: %s\n", numOfMiners);
        System.out.printf("Casper Checkpoint Space: %s blocks\n", checkpointSpace);
        System.out.printf("Transaction Generation Rate: %s tps\n", txGenerationRate);
        System.out.printf("Block Generation Rate: %s bps\n", blockGenerationRate);
        System.out.printf("Total Simulation Time: %s sec\n", simulationStopTime);
        System.out.print("** Results **\n");
        System.out.printf("Average Finalization Time : %s ms\n", blockFinalizationTimes.getMean());
        System.out.printf("Standard Deviation Finalization Time : %s ms\n", blockFinalizationTimes.getStandardDeviation());
        System.out.printf("Total Vote Traffic : %s byte\n", totalVoteMassageTraffic);
        System.out.printf("Percentage of Finalized Blocks: %s\n", ((double) ((DeterministicFinalityConsensus) ((BlockchainNode) network.getNode(0)).getConsensusAlgorithm()).getNumOfFinalizedBlocks()) / ((double) ((BlockchainNode) network.getNode(0)).numberOfAlreadySeenBlocks()));
    }

    @Override
    protected boolean csvOutputConditionBeforeEvent() {
        return false; // never output anything before execution of events
    }

    @Override
    protected boolean csvOutputConditionAfterEvent() {
        return true; // always output one line to CSV file after execution of an event
    }

    @Override
    protected String[] csvHeaderOutput() {
        return new String[0];
    }

    @Override
    protected String[] csvLineOutput() {
        return new String[0];
    }
}
