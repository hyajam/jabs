package jabs.scenario;

import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.networks.stats.sixglobalregions.SixRegions;
import jabs.network.networks.stats.sixglobalregions.ethereum.EthereumProofOfWorkGlobalNetworkStats6Regions;
import jabs.simulator.event.PacketDeliveryEvent;
import jabs.log.AbstractLogger;
import jabs.network.message.VoteMessage;
import jabs.network.networks.DAGsperGlobalBlockchainNetwork;
import jabs.simulator.event.TxGenerationProcessRandomNetworkNode;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class EthereumDAGsperNetworkScenario extends AbstractScenario {
    double simulationTime = 0;
    long totalVoteMassageTraffic = 0;

    public DescriptiveStatistics blockFinalizationTimes = new DescriptiveStatistics();

    public final int numOfMiners;
    public final int numOfNonMiners;
    public final int checkpointSpace;
    public final double simulationStopTime;
    public final double txGenerationRate;
    public final double blockGenerationRate;

    public EthereumDAGsperNetworkScenario(long seed, AbstractLogger logger, int numOfMiners, int numOfNonMiners, int checkpointSpace,
                                          double simulationStopTime, double txGenerationRate, double blockGenerationRate) {
        super("Ethereum DAGsper Network", seed, logger);
        this.numOfMiners = numOfMiners;
        this.numOfNonMiners = numOfNonMiners;
        this.checkpointSpace = checkpointSpace;
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    public void createNetwork() {
        this.network = new DAGsperGlobalBlockchainNetwork<>(randomnessEngine, checkpointSpace,
                new EthereumProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
        ((GlobalProofOfWorkNetwork<SixRegions>) network).populateNetwork(simulator, numOfMiners, numOfNonMiners);
    }

    @Override
    protected void insertInitialEvents() {
        ((DAGsperGlobalBlockchainNetwork<?>) network).startAllMiningProcesses();
        simulator.putEvent(new TxGenerationProcessRandomNetworkNode(simulator, network, randomnessEngine,
                1/txGenerationRate), 0);
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
//                    ((peerBlockchainNode) network.getAllNodes().get(0)).numberOfAlreadySeenBlocks()
//                    , ((DeterministicFinalityConsensus)
//                            ((peerBlockchainNode) network.getAllNodes().get(0)).getConsensusAlgorithm()
//                    ).getNumOfFinalizedBlocks());
//        }
        return (simulator.getCurrentTime() > simulationStopTime*1000);
    }
}
