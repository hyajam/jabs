package main.java.scenario;

import main.java.consensus.CasperFFG;
import main.java.consensus.DeterministicFinalityConsensus;
import main.java.event.RandomNodeBlockGeneratorProcess;
import main.java.event.RandomNodeTxGeneratorProcess;
import main.java.network.Network;
import main.java.network.NetworkBuilder;
import main.java.node.nodes.BlockchainNode;
import main.java.node.nodes.Node;
import main.java.simulator.AbstractSimulator;

public class NormalEthereumNetworkScenario extends AbstractScenario {
    long simulationTime = 0;

    @Override
    public void simulationSetup() {
        NetworkBuilder.buildSampleEthereumNetwork(0, 40);
        AbstractSimulator.putEvent(new RandomNodeTxGeneratorProcess(500, 500), AbstractSimulator.getCurrentTime());
        AbstractSimulator.putEvent(new RandomNodeBlockGeneratorProcess(500, 500), AbstractSimulator.getCurrentTime());
    }

    @Override
    public boolean simulationStopCondition() {
        if (AbstractSimulator.getCurrentTime() - 10000 >= simulationTime) {
            simulationTime = AbstractSimulator.getCurrentTime();
            System.out.printf("simulation time: %s, number of already seen blocks: %s, number of finalized blocks: %s\n",
                    simulationTime,
                    ((BlockchainNode) Network.getAllNodes().get(0)).numberOfAlreadySeenBlocks()
                    , ((DeterministicFinalityConsensus)
                            ((BlockchainNode) Network.getAllNodes().get(0)).getConsensusAlgorithm()
                    ).getNumOfFinalizedBlocks());
        }
        return AbstractSimulator.thereIsMoreEvents();
    }

    @Override
    public void postSimulation() {
        System.out.printf("number of already seen blocks: %s, number of finalized blocks: %s\n",
                ((BlockchainNode) Network.getAllNodes().get(0)).numberOfAlreadySeenBlocks()
                , ((DeterministicFinalityConsensus)
                        ((BlockchainNode) Network.getAllNodes().get(0)).getConsensusAlgorithm()
                ).getNumOfFinalizedBlocks());
    }
}
