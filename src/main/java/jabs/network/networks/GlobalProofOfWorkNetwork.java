package jabs.network.networks;

import jabs.network.networks.stats.MinerGlobalRegionDistribution;
import jabs.network.networks.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.node.nodes.MinerNode;
import jabs.network.node.nodes.Node;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

import java.util.ArrayList;
import java.util.List;

public abstract class GlobalProofOfWorkNetwork<Region extends Enum<Region>> extends GlobalNetwork<Region> {
    protected final List<MinerNode> miners = new ArrayList<>();
    protected final MinerGlobalRegionDistribution<Region> minerDistribution;

    protected GlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine, ProofOfWorkGlobalNetworkStats<Region> networkStats) {
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
    public Region sampleMinerRegion() {
        return minerDistribution.sampleMinerRegion();
    }
    public MinerNode getMiner(int i) {
        return miners.get(i);
    }

    public void addMiner(MinerNode node) {
        nodes.add((Node) node);
        miners.add(node);
        nodeTypes.put((Node) node, sampleMinerRegion());
    }

    protected long sampleHashPower() {
        return minerDistribution.sampleMinerHashPower();
    }

    public abstract void populateNetwork(Simulator simulator, int numMiners, int numNonMiners);
}
