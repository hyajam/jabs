package main.java;

import main.java.random.Random;
import main.java.scenario.AbstractScenario;
import main.java.scenario.EthereumCasperNetworkScenario;

public class Main {
    public static void main(String[] args) {
        AbstractScenario scenario;
        Random.setSeed(100);
        scenario = new EthereumCasperNetworkScenario(40, 100, 10, 100, 1, 2);
        scenario.run();

        Random.setSeed(100);
        scenario = new EthereumCasperNetworkScenario(40, 100, 10, 100, 1, 2);
        scenario.run();
    }
}
