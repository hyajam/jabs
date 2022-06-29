package jabs.scenario;

import jabs.consensus.config.NakamotoConsensusConfig;
import jabs.log.AbstractLogger;
import jabs.network.networks.bitcoin.BitcoinGlobalProofOfWorkNetworkWithoutTx;
import jabs.network.stats.sixglobalregions.bitcoin.BitcoinProofOfWorkGlobalNetworkStats6Regions;

public class BitcoinGlobalNetworkScenario extends AbstractScenario {
    public final double stopTime;
    public final double averageBlockInterval;
    public final int confirmationDepth;

    /**
     * creates a Bitcoin network scenario with parameters close to real-world but excluding transaction simulation for
     * better simulation speed
     *
     * @param name                 determines the name of simulation scenario
     * @param seed                 this value gives the simulation seed value for randomness engine
     * @param logger               this is output log of the scenario
     * @param stopTime             this determines how many seconds of simulation world time should it last.
     * @param averageBlockInterval This determines the interval between two block generations in seconds.
     * @param confirmationDepth    The depth at which a block is considered confirmed (eg. 6)
     */
    public BitcoinGlobalNetworkScenario(String name, long seed, AbstractLogger logger, long stopTime,
                                        double averageBlockInterval, int confirmationDepth) {
        super(name, seed, logger);
        this.stopTime = stopTime;
        this.averageBlockInterval = averageBlockInterval;
        this.confirmationDepth = confirmationDepth;
    }

    /**
     * Creates the network and populates it with miners and nodes almost equal to the real world.
     */
    @Override
    protected void createNetwork() {
        BitcoinGlobalProofOfWorkNetworkWithoutTx<?> bitcoinNetwork = new BitcoinGlobalProofOfWorkNetworkWithoutTx<>
                (randomnessEngine, new BitcoinProofOfWorkGlobalNetworkStats6Regions(randomnessEngine));
        this.network = bitcoinNetwork;
        bitcoinNetwork.populateNetwork(simulator,
                new NakamotoConsensusConfig(this.averageBlockInterval, confirmationDepth));
    }

    /**
     * Starts mining in the network by creating mining processes for each node.
     */
    @Override
    protected void insertInitialEvents() {
        ((BitcoinGlobalProofOfWorkNetworkWithoutTx<?>) network).startAllMiningProcesses();
    }

    /**
     * Stops simulation after `stopTime`
     * @return true if stopTime is passed
     */
    @Override
    protected boolean simulationStopCondition() {
        return simulator.getCurrentTime() > stopTime;
    }
}
