package jabs.log;

import jabs.consensus.DeterministicFinalityConsensus;
import jabs.node.nodes.BlockchainNode;
import jabs.scenario.AbstractScenario;
import jabs.scenario.EthereumCasperNetworkScenario;

public class CasperFinalLogger extends AbstractLogger {
    private AbstractScenario scenario;

    @Override
    public void setScenario(AbstractScenario scenario) {
        this.scenario = scenario;
    }

    @Override
    public void initialLog() {

    }

    @Override
    public void logBeforeEvent() {

    }

    @Override
    public void logAfterEvent() {

    }

    @Override
    public void finalLog() {
        if (scenario instanceof EthereumCasperNetworkScenario) {
            EthereumCasperNetworkScenario CasperScenario = (EthereumCasperNetworkScenario) scenario;
            System.out.print("*** Ethereum CasperFFG Scenario ***\n");
            System.out.print("** Settings **\n");
            System.out.printf("Total Number of Nodes: %s\n", CasperScenario.numOfMiners + CasperScenario.numOfNonMiners);
            System.out.printf("Total Number of Miners: %s\n", CasperScenario.numOfMiners);
            System.out.printf("Casper Checkpoint Space: %s blocks\n", CasperScenario.checkpointSpace);
            System.out.printf("Transaction Generation Rate: %s tps\n", CasperScenario.txGenerationRate);
            System.out.printf("Block Generation Rate: %s bps\n", CasperScenario.blockGenerationRate);
            System.out.printf("Total Simulation Time: %s sec\n", CasperScenario.simulationStopTime);
            System.out.print("** Results **\n");
            System.out.printf("Average Finalization Time : %s ms\n", CasperScenario.blockFinalizationTimes.getMean());
            System.out.printf("Standard Deviation Finalization Time : %s ms\n", CasperScenario.blockFinalizationTimes.getStandardDeviation());
            System.out.printf("Total Vote Traffic : %s byte\n", CasperScenario.totalVoteMassageTraffic);
            System.out.printf("Percentage of Finalized Blocks: %s\n", ((double) ((DeterministicFinalityConsensus) ((BlockchainNode) this.scenario.getNetwork().getNode(0)).getConsensusAlgorithm()).getNumOfFinalizedBlocks()) / ((double) ((BlockchainNode) this.scenario.getNetwork().getNode(0)).numberOfAlreadySeenBlocks()));
        }
    }
}
