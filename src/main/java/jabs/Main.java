package jabs;

import jabs.log.PBFTCSVLogger;
import jabs.scenario.AbstractScenario;
import jabs.scenario.PBFTLANScenario;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        AbstractScenario scenario;

        PBFTCSVLogger logger = new PBFTCSVLogger(Paths.get("_output/simulation-log.csv"));

        scenario = new PBFTLANScenario("PBFT Network with 4 nodes",1234, logger, 4, 10000);
        scenario.run();
    }
}
