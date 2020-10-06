package main.java.event;

import main.java.random.Random;
import main.java.simulator.Simulator;

import main.java.network.Network;

public class EventFactory {
    public static void createBlockGenerationEvents(Simulator simulator, Network network, int num, long timeBetweenGenerations) {
        long time = 0;
        for (int i = 0; i < num; i++) {
            time += Random.sampleExponentialDistribution(timeBetweenGenerations);
            simulator.putEvent(new BlockGenerationEvent(network.getRandomMinerByHashPower(), simulator), time);
        }
    }

    public static void createTxGenerationEvents(Simulator simulator, Network network, int num, long timeBetweenGenerations) {
        long time = 0;
        for (int i = 0; i < num; i++) {
            time += Random.sampleExponentialDistribution(timeBetweenGenerations);
            simulator.putEvent(new TxGenerationEvent(network.getRandomNode()), time);
        }
    }
}
