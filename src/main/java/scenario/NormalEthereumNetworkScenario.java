package main.java.scenario;

import main.java.event.RandomNodeBlockGeneratorProcess;
import main.java.event.RandomNodeTxGeneratorProcess;
import main.java.network.Network;
import main.java.network.NetworkBuilder;
import main.java.node.nodes.BlockchainNode;
import main.java.simulator.AbstractSimulator;

public class NormalEthereumNetworkScenario extends AbstractScenario {
    long simulationTime = 0;

    @Override
    public void simulationSetup() {
        NetworkBuilder.buildSampleEthereumNetwork(100,40);
        AbstractSimulator.putEvent(new RandomNodeTxGeneratorProcess(1000, 140), AbstractSimulator.getCurrentTime());
        AbstractSimulator.putEvent(new RandomNodeBlockGeneratorProcess(14000, 100), AbstractSimulator.getCurrentTime());
    }

    @Override
    public boolean simulationStopCondition() {
        if (AbstractSimulator.getCurrentTime() - 10000 >= simulationTime) {
            simulationTime = AbstractSimulator.getCurrentTime();
            System.out.printf("simulation time: %s, number of already seen blocks for miner 0: %s\n",
                    simulationTime,
                    ((BlockchainNode) Network.getAllNodes().get(0)).numberOfAlreadySeenBlocks());
        }
        return AbstractSimulator.thereIsMoreEvents();
    }

    @Override
    public void postSimulation() {

    }
}
