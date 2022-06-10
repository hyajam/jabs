package jabs.scenario;

import jabs.consensus.algorithm.CasperFFG;
import jabs.network.networks.BitcoinGlobalProofOfWorkNetworkWithoutTx;
import jabs.network.networks.GlobalNetwork;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.networks.stats.sixglobalregions.bitcoin.BitcoinProofOfWorkGlobalNetworkStats6Regions;
import jabs.network.networks.stats.sixglobalregions.ethereum.EthereumNodeGlobalNetworkStats6Regions;
import jabs.network.networks.stats.sixglobalregions.ethereum.EthereumProofOfWorkGlobalNetworkStats6Regions;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.simulator.event.PacketDeliveryEvent;
import jabs.log.AbstractLogger;
import jabs.network.message.VoteMessage;
import jabs.network.networks.CasperFFGGlobalBlockchainNetwork;
import jabs.simulator.event.TxGenerationProcessRandomNetworkNode;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class EthereumCasperNetworkScenario extends AbstractScenario {
    public final int numOfMiners;
    public final int numOfNonMiners;
    public final int checkpointSpace;
    public final double simulationStopTime;
    public final double txGenerationRate;
    public final double blockGenerationRate;

    public double simulationTime = 0;
    public long totalVoteMassageTraffic = 0;

    public DescriptiveStatistics blockFinalizationTimes = new DescriptiveStatistics();

    public EthereumCasperNetworkScenario(long seed, AbstractLogger logger, int numOfMiners, int numOfNonMiners, int checkpointSpace,
                                         long simulationStopTime, double txGenerationRate, double blockGenerationRate) {
        super("Ethereum Casper Network", seed, logger);
        this.numOfMiners = numOfMiners;
        this.numOfNonMiners = numOfNonMiners;
        this.checkpointSpace = checkpointSpace;
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    public void createNetwork() {
        this.network = new CasperFFGGlobalBlockchainNetwork(randomnessEngine, checkpointSpace,
                new EthereumProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
        ((GlobalProofOfWorkNetwork) network).populateNetwork(simulator, numOfMiners, numOfNonMiners);
    }

    @Override
    protected void insertInitialEvents() {
        ((CasperFFGGlobalBlockchainNetwork<?>) network).startAllMiningProcesses();
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
        return (simulator.getCurrentTime() > simulationStopTime*1000);
    }
}
