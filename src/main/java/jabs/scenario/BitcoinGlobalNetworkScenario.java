package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.networks.BitcoinGlobalProofOfWorkNetworkWithoutTx;
import jabs.network.networks.stats.sixglobalregions.bitcoin.BitcoinProofOfWorkGlobalNetworkStats6Regions;

public class BitcoinGlobalNetworkScenario extends AbstractScenario {
    public final double simulationStopTime;
    public final double averageBlockInterval;

    /**
     * creates an abstract scenario with a user defined name
     *
     * @param name   determines the name of simulation scenario
     * @param seed   this value gives the simulation seed value for randomness engine
     * @param logger this is output log of the scenario
     */
    public BitcoinGlobalNetworkScenario(String name, long seed, AbstractLogger logger, long simulationStopTime, double averageBlockInterval) {
        super(name, seed, logger);
        this.simulationStopTime = simulationStopTime;
        this.averageBlockInterval = averageBlockInterval;
    }

    @Override
    protected void createNetwork() {
        BitcoinGlobalProofOfWorkNetworkWithoutTx<?> bitcoinNetwork = new BitcoinGlobalProofOfWorkNetworkWithoutTx<>
                (randomnessEngine, new BitcoinProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
        this.network = bitcoinNetwork;
        bitcoinNetwork.populateNetwork(simulator, this.averageBlockInterval);
    }

    @Override
    protected void insertInitialEvents() {
        ((BitcoinGlobalProofOfWorkNetworkWithoutTx<?>) network).startAllMiningProcesses();
    }

    @Override
    protected boolean simulationStopCondition() {
        return simulator.getCurrentTime() > simulationStopTime;
    }
}
