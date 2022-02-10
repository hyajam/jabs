package jabs.network;

import jabs.node.nodes.MinerNode;
import jabs.node.nodes.Node;
import jabs.randengine.RandomnessEngine;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockchainNetwork extends Network {
    protected final List<MinerNode> miners = new ArrayList<>();
    protected long totalHashPower = 0;
    protected final List<Long> minersHashPower = new ArrayList<>();

    protected BlockchainNetwork(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    public List<MinerNode> getAllMiners() {
        return miners;
    }

    public MinerNode getMiner(int i) {
        return miners.get(i);
    }

    public long getTotalHashPower() {
        return totalHashPower;
    }

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
    }

    protected abstract long sampleHashPower();
}
