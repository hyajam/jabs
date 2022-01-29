package jabs;

import jabs.scenario.AbstractScenario;
import jabs.scenario.PBFTLANScenario;

public class Main {
    public static void main(String[] args) {
        AbstractScenario scenario;

        scenario = new PBFTLANScenario(1234);
        scenario.run();
    }
}
