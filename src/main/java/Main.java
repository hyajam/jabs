package main.java;

import main.java.event.RandomNodeBlockGeneratorProcess;
import main.java.event.RandomNodeTxGeneratorProcess;
import main.java.network.NetworkBuilder;
import main.java.simulator.AbstractSimulator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import static main.java.simulator.AbstractSimulator.*;

public class Main {
    public static RandomGenerator random = new MersenneTwister(35554);

    public static void main(String[] args) {
        NetworkBuilder.buildSampleEthereumNetwork();
        AbstractSimulator.putEvent(new RandomNodeTxGeneratorProcess(500, 20), AbstractSimulator.getCurrentTime());
        AbstractSimulator.putEvent(new RandomNodeBlockGeneratorProcess(1000, 10), AbstractSimulator.getCurrentTime());

        long lastSecond = 0;
        while (thereIsMoreEvents()) {
            if ((AbstractSimulator.getCurrentTime() - lastSecond) >= 1000){
                System.out.println(AbstractSimulator.getCurrentTime());
                lastSecond = AbstractSimulator.getCurrentTime();
            }
            executeNextEvent();
        }
    }
}
