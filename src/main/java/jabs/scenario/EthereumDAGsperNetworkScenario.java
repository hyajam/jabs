package jabs.scenario;

import jabs.consensus.DAGsper;
import jabs.consensus.DeterministicFinalityConsensus;
import jabs.event.PacketDeliveryEvent;
import jabs.message.VoteMessage;
import jabs.network.BlockchainNetwork;
import jabs.network.DAGsperGlobalBlockchainNetwork;
import jabs.network.GlobalBlockchainNetwork;
import jabs.node.nodes.BlockchainNode;
import jabs.node.nodes.Node;
import jabs.random.Random;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static jabs.event.EventFactory.createBlockGenerationEvents;
import static jabs.event.EventFactory.createTxGenerationEvents;

public class EthereumDAGsperNetworkScenario extends AbstractScenario {
    long simulationTime = 0;
    long totalVoteMassageTraffic = 0;

    DescriptiveStatistics blockFinalizationTimes = new DescriptiveStatistics();

    private final int numOfMiners;
    private final int numOfNonMiners;
    private final int checkpointSpace;
    private final long simulationStopTime;
    private final double txGenerationRate;
    private final double blockGenerationRate;

    public EthereumDAGsperNetworkScenario(long seed, int numOfMiners, int numOfNonMiners, int checkpointSpace,
                                          long simulationStopTime, double txGenerationRate, double blockGenerationRate) {
        this.random = new Random(seed);
        this.numOfMiners = numOfMiners;
        this.numOfNonMiners = numOfNonMiners;
        this.checkpointSpace = checkpointSpace;
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    public void createNetwork() {
        this.network = new DAGsperGlobalBlockchainNetwork(random, checkpointSpace);
        ((GlobalBlockchainNetwork) network).populateNetwork(simulator, numOfMiners, numOfNonMiners);

        for (Node node:network.getAllNodes()) {
            ((DAGsper) ((BlockchainNode) node).getConsensusAlgorithm()).enableFinalizationTimeRecords(blockFinalizationTimes);
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
//        if (simulator.getCurrentTime() - 10000 >= simulationTime) {
//            simulationTime = simulator.getCurrentTime();
//            System.out.printf("\rsimulation time: %s, number of already seen blocks for miner 0: %s, number of finalized blocks: %s\n",
//                    simulationTime,
//                    ((BlockchainNode) network.getAllNodes().get(0)).numberOfAlreadySeenBlocks()
//                    , ((DeterministicFinalityConsensus)
//                            ((BlockchainNode) network.getAllNodes().get(0)).getConsensusAlgorithm()
//                    ).getNumOfFinalizedBlocks());
//        }
        return (simulator.getCurrentTime() > simulationStopTime*1000);
    }

    @Override
    public void finishSimulation() {
        System.out.print("*** Ethereum DAGsper Scenario ***\n");
        System.out.print("** Settings **\n");
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
}
