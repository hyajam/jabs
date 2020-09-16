package main.java.scenario;

import main.java.Main;
import main.java.consensus.DeterministicFinalityConsensus;
import main.java.event.MessageDeliveryEvent;
import main.java.event.RandomNodeBlockGeneratorProcess;
import main.java.event.RandomNodeTxGeneratorProcess;
import main.java.message.VoteMessage;
import main.java.network.Network;
import main.java.network.NetworkBuilder;
import main.java.node.nodes.BlockchainNode;
import main.java.simulator.Simulator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class EthereumCasperNetworkScenario extends AbstractScenario {
    long simulationTime = 0;
    long totalVoteMassageTraffic = 0;

    @Override
    public void setupSimulation() {
        NetworkBuilder.buildSampleEthereumCasperNetwork(100,40, 10);
        Simulator.putEvent(new RandomNodeTxGeneratorProcess(1000, 100), Simulator.getCurrentTime());
        Simulator.putEvent(new RandomNodeBlockGeneratorProcess(1000, 100), Simulator.getCurrentTime());
    }

    @Override
    public boolean simulationStopCondition() {
        if (Simulator.peekEvent() instanceof MessageDeliveryEvent) {
            if (((MessageDeliveryEvent) Simulator.peekEvent()).packet.getMessage() instanceof VoteMessage) {
                totalVoteMassageTraffic += ((MessageDeliveryEvent) Simulator.peekEvent()).packet.getSize();
            }
        }
        if (Simulator.getCurrentTime() - 10000 >= simulationTime) {
            simulationTime = Simulator.getCurrentTime();
            System.out.printf("simulation time: %s, number of already seen blocks for miner 0: %s, number of finalized blocks: %s\n",
                    simulationTime,
                    ((BlockchainNode) Network.getAllNodes().get(0)).numberOfAlreadySeenBlocks()
                    , ((DeterministicFinalityConsensus)
                            ((BlockchainNode) Network.getAllNodes().get(0)).getConsensusAlgorithm()
                    ).getNumOfFinalizedBlocks());
        }
        return Simulator.thereIsMoreEvents();
    }

    @Override
    public void outputResults() {
        DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();

        for (Double time: Main.timeToFinalize){
            descriptiveStatistics.addValue(time);
        }

        System.out.printf("Average Finalization Time : %s\n", descriptiveStatistics.getMean());
        System.out.printf("Standard Deviation Finalization Time : %s\n", descriptiveStatistics.getStandardDeviation());
        System.out.printf("Total Vote Traffic : %s\n", totalVoteMassageTraffic);
        System.out.printf("Finalization Events: %s\n", Main.timeToFinalize.size());
    }
}
