package jabs.scenario;

import jabs.consensus.CasperFFG;
import jabs.event.PacketDeliveryEvent;
import jabs.log.AbstractLogger;
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
    public final int numOfMiners;
    public final int numOfNonMiners;
    public final int checkpointSpace;
    public final long simulationStopTime;
    public final double txGenerationRate;
    public final double blockGenerationRate;

    public long simulationTime = 0;
    public long totalVoteMassageTraffic = 0;

    public DescriptiveStatistics blockFinalizationTimes = new DescriptiveStatistics();

    public EthereumCasperNetworkScenario(long seed, AbstractLogger logger, int numOfMiners, int numOfNonMiners, int checkpointSpace,
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
        this.network = new CasperFFGGlobalBlockchainNetwork(randomnessEngine, checkpointSpace);
        ((GlobalBlockchainNetwork) network).populateNetwork(simulator, numOfMiners, numOfNonMiners);

        for (Node node:network.getAllNodes()) {
            ((CasperFFG) ((BlockchainNode) node).getConsensusAlgorithm()).enableFinalizationTimeRecords(blockFinalizationTimes);
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
        return (simulator.getCurrentTime() > simulationStopTime*1000);
    }
}
