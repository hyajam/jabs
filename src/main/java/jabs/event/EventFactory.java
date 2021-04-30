package jabs.event;

import jabs.network.BlockchainNetwork;
import jabs.random.Random;
import jabs.simulator.Simulator;

import jabs.network.Network;

public class EventFactory {
    public static void createBlockGenerationEvents(Simulator simulator, Random random, BlockchainNetwork network, int num, long timeBetweenGenerations) {
        long time = 0;
        for (int i = 0; i < num; i++) {
            time += random.sampleExponentialDistribution(timeBetweenGenerations);
            simulator.putEvent(new BlockGenerationEvent(network.getRandomMinerByHashPower(), simulator), time);
        }
    }

    public static void createTxGenerationEvents(Simulator simulator, Random random, Network network, int num, long timeBetweenGenerations) {
        long time = 0;
        for (int i = 0; i < num; i++) {
            time += random.sampleExponentialDistribution(timeBetweenGenerations);
            simulator.putEvent(new TxGenerationEvent(network.getRandomNode()), time);
        }
    }
}
