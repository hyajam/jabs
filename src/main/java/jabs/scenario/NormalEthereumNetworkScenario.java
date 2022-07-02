package jabs.scenario;

import jabs.consensus.config.GhostProtocolConfig;
import jabs.log.AbstractLogger;
import jabs.network.networks.ethereum.EthereumGlobalProofOfWorkNetwork;
import jabs.network.stats.sixglobalregions.ethereum.EthereumProofOfWorkGlobalNetworkStats6Regions;

public class NormalEthereumNetworkScenario extends AbstractScenario {
    private final double simulationStopTime;
    private final double averageBlockInterval;

    /**
     * @param name
     * @param seed
     * @param simulationStopTime
     * @param averageBlockInterval
     */
    public NormalEthereumNetworkScenario(String name, long seed,
                                         double simulationStopTime, double averageBlockInterval) {
        super(name, seed);
        this.simulationStopTime = simulationStopTime;
        this.averageBlockInterval = averageBlockInterval;
    }

    @Override
    public void createNetwork() {
        EthereumGlobalProofOfWorkNetwork<?> ethereumNetwork = new EthereumGlobalProofOfWorkNetwork<>(randomnessEngine,
                new EthereumProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
        this.network = ethereumNetwork;
        ethereumNetwork.populateNetwork(simulator, new GhostProtocolConfig(this.averageBlockInterval));
    }

    @Override
    protected void insertInitialEvents() {
        ((EthereumGlobalProofOfWorkNetwork<?>) network).startAllMiningProcesses();
    }

    @Override
    public boolean simulationStopCondition() {
        return (simulator.getCurrentTime() > simulationStopTime);
    }
}
