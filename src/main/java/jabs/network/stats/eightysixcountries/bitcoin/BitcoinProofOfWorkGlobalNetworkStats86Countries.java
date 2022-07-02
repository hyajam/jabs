package jabs.network.stats.eightysixcountries.bitcoin;

import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.stats.eightysixcountries.EightySixCountries;
import jabs.simulator.randengine.RandomnessEngine;

import static jabs.network.stats.sixglobalregions.ethereum.EthereumNodeGlobalNetworkStats6Regions.ETHEREUM_NUM_NODES_2022;

public class BitcoinProofOfWorkGlobalNetworkStats86Countries extends BitcoinNodeGlobalNetworkStats86Countries
        implements ProofOfWorkGlobalNetworkStats<EightySixCountries> {

    public BitcoinProofOfWorkGlobalNetworkStats86Countries(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    public static final double[] BITCOIN_MINER_REGION_DISTRIBUTION_2020 = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0.158, 0, 0.4906, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.269, 0, 0, 0, 0, 0, 0, 0, 0,
            0.467, 0.207, 0, 0, 0, 0.765, 0, 0, 0, 0, 0, 0, 0.366, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.647,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.1612, 0, 0, 0, 0.602
    };

    /**
     * Hash power probability distribution (CDF) in Bitcoin Network
     */
    public static final double[] BITCOIN_HASH_POWER_DISTRIBUTION_2022 = {
            0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625, 0.0625
    };

    /**
     * Hash power probability distribution (Hash Power Values) in Bitcoin Network presented in ExaHash per second
     */
    public static final long[] BITCOIN_HASH_POWER_DISTRIBUTION_BIN_2022 = {
            50, 37, 33, 23, 22, 17, 13, 10, 8, 5, 2, 1, 1, 1, 1, 1
    };

    public static final int BITCOIN_NUM_MINERS_2022 = 30;

    @Override
    public EightySixCountries sampleMinerRegion() {
        return EightySixCountries.sixRegionsValues[randomnessEngine.sampleFromDistribution(BITCOIN_MINER_REGION_DISTRIBUTION_2020)];
    }

    @Override
    public long sampleMinerHashPower() {
        return randomnessEngine.sampleDistributionWithBins(BITCOIN_HASH_POWER_DISTRIBUTION_2022, BITCOIN_HASH_POWER_DISTRIBUTION_BIN_2022);
    }

    /**
     * @return The share of miner nodes to all nodes in the Bitcoin network
     */
    @Override
    public double shareOfMinersToAllNodes() {
        return BITCOIN_NUM_MINERS_2022/(double)BITCOIN_NUM_NODES_2022;
    }

    @Override
    public int totalNumberOfMiners() {
        return BITCOIN_NUM_MINERS_2022;
    }
}
