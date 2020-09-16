package main.java;

import main.java.random.Random;
import main.java.scenario.AbstractScenario;
import main.java.scenario.EthereumCasperNetworkScenario;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public final static List<Double> timeToFinalize = new ArrayList<>();

    public static void main(String[] args) {
        Random.setSeed(100);
        AbstractScenario scenario = new EthereumCasperNetworkScenario();
        scenario.run();
    }
}
