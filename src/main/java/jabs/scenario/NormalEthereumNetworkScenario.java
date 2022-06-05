package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.networks.EthereumGlobalProofOfWorkNetwork;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.networks.stats.sixglobalregions.SixRegions;
import jabs.network.networks.stats.sixglobalregions.ethereum.EthereumProofOfWorkGlobalNetworkStats6Regions;
import jabs.network.node.nodes.PeerBlockchainNode;

import static jabs.simulator.event.EventFactory.createBlockGenerationEvents;
import static jabs.simulator.event.EventFactory.createTxGenerationEvents;

public class NormalEthereumNetworkScenario extends AbstractScenario {
    double simulationTime = 0;

    private final int numOfMiners;
    private final int numOfNonMiners;
    private final double simulationStopTime;
    private final double txGenerationRate;
    private final double blockGenerationRate;

    public NormalEthereumNetworkScenario(long seed, AbstractLogger logger, int numOfMiners, int numOfNonMiners, double simulationStopTime, double txGenerationRate, double blockGenerationRate) {
        super("Normal Ethereum Network", seed, logger);
        this.numOfMiners = numOfMiners;
        this.numOfNonMiners = numOfNonMiners;
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    public void createNetwork() {
        this.network = new EthereumGlobalProofOfWorkNetwork<>(randomnessEngine,
                new EthereumProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
    }

    @Override
    protected void insertInitialEvents() {
        createTxGenerationEvents(simulator, randomnessEngine, network, ((int) (simulationStopTime*txGenerationRate)), (long)(1/txGenerationRate));
        createBlockGenerationEvents(simulator, randomnessEngine, (GlobalProofOfWorkNetwork) network, ((int) (simulationStopTime*blockGenerationRate)), (long)(1/blockGenerationRate));
    }

    @Override
    public boolean simulationStopCondition() {
        if (simulator.getCurrentTime() - 10000 >= simulationTime) {
            simulationTime = simulator.getCurrentTime();
            System.out.printf("\rsimulation time: %s, number of already seen blocks for miner 0: %s\n",
                    simulationTime,
                    ((PeerBlockchainNode) network.getAllNodes().get(0)).numberOfAlreadySeenBlocks());
        }
        return (simulator.getCurrentTime() > simulationStopTime*1000);
    }
}
