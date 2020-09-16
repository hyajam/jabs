package main.java.scenario;

import main.java.event.RandomNodeBlockGeneratorProcess;
import main.java.event.RandomNodeTxGeneratorProcess;
import main.java.network.Network;
import main.java.network.NetworkBuilder;
import main.java.node.nodes.BlockchainNode;
import main.java.simulator.Simulator;

public class NormalEthereumNetworkScenario extends AbstractScenario {
    long simulationTime = 0;

    @Override
    public void setupSimulation() {
        NetworkBuilder.buildSampleEthereumNetwork(100,40);
        Simulator.putEvent(new RandomNodeTxGeneratorProcess(1000, 140), Simulator.getCurrentTime());
        Simulator.putEvent(new RandomNodeBlockGeneratorProcess(14000, 100), Simulator.getCurrentTime());
    }

    @Override
    public boolean simulationStopCondition() {
        if (Simulator.getCurrentTime() - 10000 >= simulationTime) {
            simulationTime = Simulator.getCurrentTime();
            System.out.printf("simulation time: %s, number of already seen blocks for miner 0: %s\n",
                    simulationTime,
                    ((BlockchainNode) Network.getAllNodes().get(0)).numberOfAlreadySeenBlocks());
        }
        return Simulator.thereIsMoreEvents();
    }

    @Override
    public void outputResults() {

    }
}
