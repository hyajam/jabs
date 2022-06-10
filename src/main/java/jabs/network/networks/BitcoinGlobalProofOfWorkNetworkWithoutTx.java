package jabs.network.networks;

import jabs.ledgerdata.bitcoin.BitcoinBlock;
import jabs.network.networks.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.node.nodes.Node;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;

public class BitcoinGlobalProofOfWorkNetworkWithoutTx<R extends Enum<R>> extends BitcoinGlobalProofOfWorkNetwork<R> {
    public BitcoinGlobalProofOfWorkNetworkWithoutTx(RandomnessEngine randomnessEngine,
                                                    ProofOfWorkGlobalNetworkStats<R> networkStats) {
        super(randomnessEngine, networkStats);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numNodes) {
        int numMiners = (int) Math.floor(0.01 * (float)numNodes) + 1;
        this.populateNetwork(simulator, numMiners, numNodes-numMiners);
    }

    @Override
    public void populateNetwork(Simulator simulator, int numMiners, int numNonMiners) {
        long totalHashPower = 0;
        List<Long> hashPowers = new ArrayList<>();
        for (int i = 0; i < numMiners; i++) {
            long hashPower = sampleHashPower();
            hashPowers.add(hashPower);
            totalHashPower += hashPower;
        }

        double difficulty = totalHashPower * bitcoinBlockGenerationInterval;
        BitcoinBlock genesisBlock = new BitcoinBlock(0, 0, simulator.getCurrentTime(), null,
                null, difficulty);

        for (int i = 0; i < numMiners; i++) {
            this.addMiner(createSampleBitcoinMinerNodeWithoutTx(simulator, this, i, genesisBlock,
                    hashPowers.get(i)));
        }

        for (int i = 0; i < numNonMiners; i++) {
            this.addNode(createSampleBitcoinNode(simulator, this, numMiners + i, genesisBlock));
        }

        for (Node node:this.getAllNodes()) {
            node.getP2pConnections().connectToNetwork(this);
        }
    }
}
