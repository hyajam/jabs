package jabs;

import jabs.log.AbstractLogger;
import jabs.log.BlockGenerationLogger;
import jabs.scenario.AbstractScenario;
import jabs.scenario.BitcoinGlobalNetworkScenario;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        AbstractScenario scenario;
        AbstractLogger logger;

        logger = new BlockGenerationLogger(Paths.get("_output/simulation-log.csv"));
        scenario = new BitcoinGlobalNetworkScenario("Normal Bitcoin Network", 1, logger,
                60000, 600);

        scenario.run();
    }
}

// TODO: Adding Throughput Logger
// TODO: Add Multiple Loggers and Scenarios Default
// TODO: Add New Network Latency Stats
// TODO: Finalize Network Churn with Events
// TODO: Explore the multithreading option

// TODO: Future Consensus Algorithms:
// TODO: Honey Badger BFT
// TODO: IOTA
// TODO: DPoS