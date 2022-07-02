package jabs;

import jabs.log.*;
import jabs.network.stats.sixglobalregions.GlobalNetworkStats6Region;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.scenario.AbstractScenario;
import jabs.scenario.BitcoinGlobalNetworkScenario;
import jabs.scenario.NormalEthereumNetworkScenario;
import jabs.scenario.PBFTLANScenario;
import jabs.simulator.randengine.RandomnessEngine;

import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 */
public class Main {
    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        AbstractScenario scenario;

        // Simulate one day in the life of Bitcoin network
        // Nakamoto protocol with block every 600 seconds
        // Around 8000 nodes with 30 miners
        scenario = new BitcoinGlobalNetworkScenario("One day in the life of Bitcoin", 1,
                86400, 600, 6);
        scenario.AddNewLogger(new BlockchainReorgLogger(Paths.get("_output/bitcoin-reorgs-log.csv")));
        scenario.AddNewLogger(new BlockConfirmationLogger(Paths.get("_output/bitcoin-confirmations-log.csv")));
        scenario.AddNewLogger(new BlockDeliveryLogger(Paths.get("_output/bitcoin-block-delivery-log.csv")));
        scenario.AddNewLogger(new BlockPropagationDelayLogger(
                Paths.get("_output/bitcoin-50-propagation-delay-log.csv"),0.5));
        scenario.AddNewLogger(new BlockPropagationDelayLogger(
                Paths.get("_output/bitcoin-90-propagation-delay-log.csv"),0.9));
        scenario.run();

        // Simulate 1 hour in the life of Ethereum network
        // Ghost protocol with blocks every 14 seconds on average
        // Around 6000 nodes with 37 miners
        scenario = new NormalEthereumNetworkScenario("One hour in the life of Ethereum", 1,
                3600, 14);
        scenario.AddNewLogger(new BlockGenerationLogger(Paths.get("_output/ethereum-simulation-log.csv")));
        scenario.run();

        // Simulate PBFT Lan network of 40 nodes for 1 hour
        scenario = new PBFTLANScenario("One hour of a PBFT lan Network", 1,
                40, 3600);
        scenario.AddNewLogger(new BlockGenerationLogger(Paths.get("_output/pbft-simulation-log.csv")));
        scenario.run();
    }
}
