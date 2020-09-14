package main.java;

import main.java.scenario.AbstractScenario;
import main.java.scenario.EthereumCasperNetworkScenario;
import main.java.scenario.EthereumDAGsperNetworkScenario;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import java.util.ArrayList;
import java.util.List;

import static main.java.simulator.AbstractSimulator.*;

public class Main {
    public final static RandomGenerator random = new MersenneTwister(124);
    public final static List<Double> timeToFinalize = new ArrayList<>();

    public static void main(String[] args) {
        AbstractScenario scenario = new EthereumCasperNetworkScenario();
        scenario.simulationSetup();

        while (scenario.simulationStopCondition()) {
            executeNextEvent();
        }

        scenario.postSimulation();
    }
}
