package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.networks.CasperFFGGlobalBlockchainNetwork;
import jabs.network.networks.EthereumGlobalProofOfWorkNetwork;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.networks.stats.sixglobalregions.SixRegions;
import jabs.network.networks.stats.sixglobalregions.ethereum.EthereumProofOfWorkGlobalNetworkStats6Regions;
import jabs.network.node.nodes.PeerBlockchainNode;
import jabs.simulator.event.TxGenerationProcessRandomNetworkNode;

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
        this.network.populateNetwork(simulator);
    }

    @Override
    protected void insertInitialEvents() {
        ((EthereumGlobalProofOfWorkNetwork<?>) network).startAllMiningProcesses();
        simulator.putEvent(new TxGenerationProcessRandomNetworkNode(simulator, network, randomnessEngine,
                1/txGenerationRate), 0);
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
