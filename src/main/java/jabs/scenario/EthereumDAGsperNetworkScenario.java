package jabs.scenario;

import jabs.consensus.DAGsper;
import jabs.event.PacketDeliveryEvent;
import jabs.log.AbstractLogger;
import jabs.message.VoteMessage;
import jabs.network.BlockchainNetwork;
import jabs.network.DAGsperGlobalBlockchainNetwork;
import jabs.network.GlobalBlockchainNetwork;
import jabs.node.nodes.BlockchainNode;
import jabs.node.nodes.Node;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import static jabs.event.EventFactory.createBlockGenerationEvents;
import static jabs.event.EventFactory.createTxGenerationEvents;

public class EthereumDAGsperNetworkScenario extends AbstractScenario {
    long simulationTime = 0;
    long totalVoteMassageTraffic = 0;

    public DescriptiveStatistics blockFinalizationTimes = new DescriptiveStatistics();

    public final int numOfMiners;
    public final int numOfNonMiners;
    public final int checkpointSpace;
    public final long simulationStopTime;
    public final double txGenerationRate;
    public final double blockGenerationRate;

    public EthereumDAGsperNetworkScenario(long seed, AbstractLogger logger, int numOfMiners, int numOfNonMiners, int checkpointSpace,
                                          long simulationStopTime, double txGenerationRate, double blockGenerationRate) {
        super(seed, logger);
        this.numOfMiners = numOfMiners;
        this.numOfNonMiners = numOfNonMiners;
        this.checkpointSpace = checkpointSpace;
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    public void createNetwork() {
        this.network = new DAGsperGlobalBlockchainNetwork(randomnessEngine, checkpointSpace);
        ((GlobalBlockchainNetwork) network).populateNetwork(simulator, numOfMiners, numOfNonMiners);

        for (Node node:network.getAllNodes()) {
            ((DAGsper) ((BlockchainNode) node).getConsensusAlgorithm()).enableFinalizationTimeRecords(blockFinalizationTimes);
        }
    }

    @Override
    protected void insertInitialEvents() {
        createTxGenerationEvents(simulator, randomnessEngine, network, ((int) (simulationStopTime*txGenerationRate)), (long)(1000/txGenerationRate));
        createBlockGenerationEvents(simulator, randomnessEngine, (BlockchainNetwork) network, ((int) (simulationStopTime*blockGenerationRate)), (long)(1000/blockGenerationRate));
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
}
