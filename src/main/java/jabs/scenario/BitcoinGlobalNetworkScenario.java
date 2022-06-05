package jabs.scenario;

import jabs.log.AbstractLogger;
import jabs.network.networks.BitcoinGlobalProofOfWorkNetworkWithoutTx;
import jabs.network.networks.GlobalProofOfWorkNetwork;
import jabs.network.networks.stats.sixglobalregions.bitcoin.BitcoinProofOfWorkGlobalNetworkStats6Regions;

import static jabs.simulator.event.EventFactory.createBlockGenerationEvents;

public class BitcoinGlobalNetworkScenario extends AbstractScenario {
    public final double simulationStopTime;
    public final double txGenerationRate;
    public final double blockGenerationRate;

    /**
     * creates an abstract scenario with a user defined name
     *
     * @param name   determines the name of simulation scenario
     * @param seed   this value gives the simulation seed value for randomness engine
     * @param logger this is output log of the scenario
     */
    public BitcoinGlobalNetworkScenario(String name, long seed, AbstractLogger logger, long simulationStopTime,
                                        double txGenerationRate, double blockGenerationRate) {
        super(name, seed, logger);
        this.simulationStopTime = simulationStopTime;
        this.txGenerationRate = txGenerationRate;
        this.blockGenerationRate = blockGenerationRate;
    }

    @Override
    protected void createNetwork() {
        this.network = new BitcoinGlobalProofOfWorkNetworkWithoutTx<>(randomnessEngine,
                new BitcoinProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
        network.populateNetwork(simulator, 15525);
    }

    @Override
    protected void insertInitialEvents() {
        createBlockGenerationEvents(simulator, randomnessEngine, (GlobalProofOfWorkNetwork) network,
                ((int) (simulationStopTime*blockGenerationRate)), 1/blockGenerationRate);
    }

    @Override
    protected boolean simulationStopCondition() {
        return false;
    }
}
