package jabs;

import jabs.log.AbstractLogger;
import jabs.log.BlockConfirmationLogger;
import jabs.log.BlockGenerationLogger;
import jabs.scenario.AbstractScenario;
import jabs.scenario.BitcoinGlobalNetworkScenario;
import jabs.scenario.NormalEthereumNetworkScenario;
import jabs.scenario.PBFTLANScenario;

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
        AbstractLogger logger;

        // Simulate one day in the life of Bitcoin network
        // Nakamoto protocol with block every 600 seconds
        // Around 16000 nodes with 30 miners
        logger = new BlockConfirmationLogger(Paths.get("_output/bitcoin-simulation-log.csv"));
        scenario = new BitcoinGlobalNetworkScenario("One day in the life of Bitcoin", 1, logger,
                86400, 600, 6);
        scenario.run();

        // Simulate 1 hour in the life of Ethereum network
        // Ghost protocol with blocks every 14 seconds on average
        // Around 6000 nodes with 37 miners
        logger = new BlockGenerationLogger(Paths.get("_output/ethereum-simulation-log.csv"));
        scenario = new NormalEthereumNetworkScenario("One day in the life of Ethereum", 1, logger,
                3600, 14);
        scenario.run();

        // Simulate PBFT Lan network of 40 nodes for 1 hour
        logger = new BlockGenerationLogger(Paths.get("_output/pbft-simulation-log.csv"));
        scenario = new PBFTLANScenario("24 hours of a PBFT lan Network", 1, logger,
                40, 3600);
        scenario.run();
    }
}
