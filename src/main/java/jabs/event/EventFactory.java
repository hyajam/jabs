package jabs.event;

import jabs.network.BlockchainNetwork;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import jabs.network.Network;

public class EventFactory {
    public static void createBlockGenerationEvents(Simulator simulator, RandomnessEngine randomnessEngine, BlockchainNetwork network, int num, double timeBetweenGenerations) {
        double time = 0;
        for (int i = 0; i < num; i++) {
            time += randomnessEngine.sampleExponentialDistribution(timeBetweenGenerations);
            simulator.putEvent(new BlockGenerationEvent(network.getRandomMinerByHashPower(), simulator), time);
        }
    }

    public static void createTxGenerationEvents(Simulator simulator, RandomnessEngine randomnessEngine, Network network, int num, double timeBetweenGenerations) {
        double time = 0;
        for (int i = 0; i < num; i++) {
            time += randomnessEngine.sampleExponentialDistribution(timeBetweenGenerations);
            simulator.putEvent(new TxGenerationEvent(network.getRandomNode()), time);
        }
    }
}
