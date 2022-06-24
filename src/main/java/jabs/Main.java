package jabs;

import jabs.log.AbstractLogger;
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
    public static void main(String[] args) throws IOException {
        AbstractScenario scenario;
        AbstractLogger logger;

        // Simulate one day in the life of Bitcoin network
        logger = new BlockGenerationLogger(Paths.get("_output/bitcoin-simulation-log.csv"));
        scenario = new BitcoinGlobalNetworkScenario("One day in the life of Bitcoin", 1, logger,
                86400, 600);
        scenario.run();

        // Simulate one day in the life of Ethereum network
        logger = new BlockGenerationLogger(Paths.get("_output/ethereum-simulation-log.csv"));
        scenario = new NormalEthereumNetworkScenario("One day in the life of Ethereum", 1, logger,
                86400, 600);
        scenario.run();

        // Simulate PBFT Lan network of 40 nodes for 24 hours
        logger = new BlockGenerationLogger(Paths.get("_output/pbft-simulation-log.csv"));
        scenario = new PBFTLANScenario("24 hours of a PBFT lan Network", 1, logger,
                40, 86400);
        scenario.run();
    }
}
