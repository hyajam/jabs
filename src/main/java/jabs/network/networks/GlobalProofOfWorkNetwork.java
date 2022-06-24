package jabs.network.networks;

import jabs.consensus.config.ChainBasedConsensusConfig;
import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.ledgerdata.Block;
import jabs.network.stats.MinerGlobalRegionDistribution;
import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.node.nodes.MinerNode;
import jabs.network.node.nodes.Node;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

import java.util.ArrayList;
import java.util.List;

public abstract class GlobalProofOfWorkNetwork<N extends Node, M extends MinerNode, B extends Block<B>, R extends Enum<R>>
        extends GlobalNetwork<N, R> {
    protected final List<MinerNode> miners = new ArrayList<>();
    protected final MinerGlobalRegionDistribution<R> minerDistribution;

    protected GlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine, ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
        this.minerDistribution = networkStats;
    }

    public void startAllMiningProcesses() {
        List<MinerNode> allMiners = this.getAllMiners();
        for (MinerNode miner: allMiners) {
            miner.startMining();
        }
    }

    public List<MinerNode> getAllMiners() {
        return miners;
    }
    public R sampleMinerRegion() {
        return minerDistribution.sampleMinerRegion();
    }
    public MinerNode getMiner(int i) {
        return miners.get(i);
    }

    public void addMiner(M node) {
        nodes.add((N) node);
        miners.add(node);
        nodeTypes.put((N) node, sampleMinerRegion());
    }

    protected long sampleHashPower() {
        return minerDistribution.sampleMinerHashPower();
    }

    public abstract B genesisBlock(double difficulty);
    public abstract N createSampleNode(Simulator simulator, int nodeID, B genesisBlock,
                                       ChainBasedConsensusConfig chainBasedConsensusConfig);
    public abstract M createSampleMiner(Simulator simulator, int nodeID, long hashPower, B genesisBlock,
                                        ChainBasedConsensusConfig chainBasedConsensusConfig);


    public void populateNetwork(Simulator simulator, ConsensusAlgorithmConfig consensusAlgorithmConfig) {
        this.populateNetwork(simulator, minerDistribution.totalNumberOfMiners(), nodeDistribution.totalNumberOfNodes(),
                consensusAlgorithmConfig);
    }

    public void populateNetwork(Simulator simulator, int numNodes, ConsensusAlgorithmConfig consensusAlgorithmConfig) {
        int numMiners = (int) Math.floor(minerDistribution.shareOfMinersToAllNodes() * numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners, consensusAlgorithmConfig);
    }


    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners,
                                ConsensusAlgorithmConfig consensusAlgorithmConfig) {
        long totalHashPower = 0;
        List<Long> hashPowers = new ArrayList<>();
        for (int i = 0; i < numMiners; i++) {
            long hashPower = sampleHashPower();
            hashPowers.add(hashPower);
            totalHashPower += hashPower;
        }

        ChainBasedConsensusConfig chainBasedConsensusConfig = (ChainBasedConsensusConfig) consensusAlgorithmConfig;

        double difficulty = totalHashPower * chainBasedConsensusConfig.averageBlockMiningInterval();
        B genesisBlock = this.genesisBlock(difficulty);

        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createSampleMiner(simulator, i, hashPowers.get(i), genesisBlock, chainBasedConsensusConfig));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createSampleNode(simulator, numMiners + i, genesisBlock, chainBasedConsensusConfig));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }
}
