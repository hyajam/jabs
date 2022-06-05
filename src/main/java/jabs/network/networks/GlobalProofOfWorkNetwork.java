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
    protected long totalHashPower = 0;
    protected final List<Long> minersHashPower = new ArrayList<>();
    protected final MinerGlobalRegionDistribution<Region> minerDistribution;

    protected GlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine, ProofOfWorkGlobalNetworkStats<Region> networkStats) {
        super(randomnessEngine, networkStats);
        this.minerDistribution = networkStats;
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

    public long getTotalHashPower() {
        return totalHashPower;
    }

    // TODO: following function should be removed after introducing new block generation process
    public MinerNode getRandomMinerByHashPower() {
        double[] hashPowerDistribution = new double[miners.size()];

        for (int i = 0; i < miners.size(); i++) {
            hashPowerDistribution[i] = ((double) minersHashPower.get(i)) / ((double) totalHashPower);
        }

        return miners.get(randomnessEngine.sampleFromDistribution(hashPowerDistribution));
    }

    public void addMiner(MinerNode node) {
        nodes.add((Node) node);
        miners.add(node);
        minersHashPower.add(node.getHashPower());
        totalHashPower += node.getHashPower();
        nodeTypes.put((Node) node, sampleMinerRegion());
    }

    protected long sampleHashPower() {
        return minerDistribution.sampleMinerHashPower();
    }

    public abstract void populateNetwork(Simulator simulator, int numMiners, int numNonMiners);
}
