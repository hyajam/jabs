package jabs.network.stats.sixglobalregions.ethereum;

import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class EthereumProofOfWorkGlobalNetworkStats6Regions extends EthereumNodeGlobalNetworkStats6Regions
        implements ProofOfWorkGlobalNetworkStats<SixRegions> {

    public EthereumProofOfWorkGlobalNetworkStats6Regions(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }
    public static final double[] ETHEREUM_MINER_REGION_DISTRIBUTION_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    /**
     * Hash power probability distribution (CDF) in Ethereum Network
     */
    public static final double[] ETHEREUM_HASH_POWER_DISTRIBUTION_2022 = {
            0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027,
            0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027,
            0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027,
            0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027,
            0.027027027, 0.027027027, 0.027027027, 0.027027027, 0.027027027
    };

    /**
     * Hash power probability distribution (Hash Power Values) in Ethereum Network
     */
    public static final long[] ETHEREUM_HASH_POWER_DISTRIBUTION_BIN_2022 = {
            1051, 285, 144, 114, 69, 55, 34, 30, 28, 27, 25, 22, 22, 21, 20, 16,
            15, 14, 14, 11, 11, 10, 9, 8, 6, 6, 4, 4, 4, 3, 3, 3, 2, 2, 2, 2, 1
    };

    public static final int ETHEREUM_NUM_MINERS_2022 = 37;


    @Override
    public SixRegions sampleMinerRegion() {
        return SixRegions.sixRegionsValues[randomnessEngine.sampleFromDistribution(ETHEREUM_MINER_REGION_DISTRIBUTION_2020)];
    }

    @Override
    public double sampleMinerHashPower() {
        return randomnessEngine.sampleDistributionWithBins(ETHEREUM_HASH_POWER_DISTRIBUTION_2022,
                ETHEREUM_HASH_POWER_DISTRIBUTION_BIN_2022);
    }

    /**
     * @return The share of miner nodes to all nodes in the Bitcoin network
     */
    @Override
    public double shareOfMinersToAllNodes() {
        return ETHEREUM_NUM_MINERS_2022 /(double)ETHEREUM_NUM_NODES_2022;
    }

    /**
     * @return total number of miners in Ethereum network
     */
    @Override
    public int totalNumberOfMiners() {
        return ETHEREUM_NUM_MINERS_2022;
    }
}
