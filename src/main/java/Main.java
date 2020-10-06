package main.java;

import main.java.scenario.AbstractScenario;
import main.java.scenario.EthereumCasperNetworkScenario;

public class Main {
    public static void main(String[] args) {
        AbstractScenario scenario;
        scenario = new EthereumCasperNetworkScenario(40, 100, 10, 100, 1, 2);
        scenario.run();

        scenario = new EthereumCasperNetworkScenario(40, 100, 10, 100, 1, 2);
        scenario.run();
    }
}
