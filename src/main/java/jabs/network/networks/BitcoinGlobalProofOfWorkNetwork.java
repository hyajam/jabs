package jabs.network.networks;

import jabs.ledgerdata.bitcoin.BitcoinBlock;
import jabs.network.networks.stats.*;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNodeWithoutTx;
import jabs.network.node.nodes.bitcoin.BitcoinNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;

public class BitcoinGlobalProofOfWorkNetwork<R extends Enum<R>> extends GlobalProofOfWorkNetwork<R> {
    protected static final double bitcoinBlockGenerationInterval = 600;
    public BitcoinGlobalProofOfWorkNetwork(RandomnessEngine randomnessEngine,
                                           ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    public BitcoinNode createSampleBitcoinNode(Simulator simulator, Network<R> network, int nodeID,
                                               BitcoinBlock genesisBlock) {
        R region = this.sampleRegion();
        return new BitcoinNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region),
                network.sampleUploadBandwidth(region), genesisBlock);
    }

    public BitcoinMinerNode createSampleBitcoinMinerNode(Simulator simulator, Network<R> network, int nodeID,
                                                         BitcoinBlock genesisBlock, long hashPower) {
        R region = this.sampleRegion();
        return new BitcoinMinerNode(simulator, this, nodeID, network.sampleDownloadBandwidth(region),
                network.sampleUploadBandwidth(region), genesisBlock, hashPower);
    }

    public BitcoinMinerNode createSampleBitcoinMinerNodeWithoutTx(Simulator simulator, Network<R> network, int nodeID,
                                                                  BitcoinBlock genesisBlock, long hashPower) {
        R region = this.sampleRegion();
        return new BitcoinMinerNodeWithoutTx(simulator, this, nodeID, network.sampleDownloadBandwidth(region),
                network.sampleUploadBandwidth(region), genesisBlock, hashPower);
    }

    @Override
    public void populateNetwork(Simulator simulator) {
        this.populateNetwork(simulator, minerDistribution.totalNumberOfMiners(), nodeDistribution.totalNumberOfNodes());
    }

    public void populateNetwork(Simulator simulator, double averageBlockMiningInterval) {
        this.populateNetwork(simulator, minerDistribution.totalNumberOfMiners(), nodeDistribution.totalNumberOfNodes(),
                averageBlockMiningInterval);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        this.populateNetwork(simulator, numMiners, numNonMiners, bitcoinBlockGenerationInterval);
    }

    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners, double averageBlockMiningInterval) {
        long totalHashPower = 0;
        List<Long> hashPowers = new ArrayList<>();
        for (int i = 0; i < numMiners; i++) {
            long hashPower = sampleHashPower();
            hashPowers.add(hashPower);
            totalHashPower += hashPower;
        }

        double difficulty = totalHashPower * averageBlockMiningInterval;
        BitcoinBlock genesisBlock = new BitcoinBlock(0, 0, simulator.getCurrentTime(), null, null, difficulty);

        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createSampleBitcoinMinerNode(simulator, this, i, genesisBlock, hashPowers.get(i)));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createSampleBitcoinNode(simulator, this, numMiners + i, genesisBlock));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }
}
