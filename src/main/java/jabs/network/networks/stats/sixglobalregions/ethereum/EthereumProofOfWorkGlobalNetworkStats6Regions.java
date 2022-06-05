package jabs.network.networks.stats.sixglobalregions.ethereum;

import jabs.network.networks.stats.MinerGlobalRegionDistribution;
import jabs.network.networks.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.networks.stats.sixglobalregions.SixRegions;
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
    public static final double[] ETHEREUM_HASH_POWER_DISTRIBUTION = {0.0035, 0.0035, 0.0070, 0.0140, 0.0279, 0.0558, 0.1117, 0.2234, 0.4468, 0.1065};

    /**
     * Hash power probability distribution (Hash Power Values) in Ethereum Network
     */
    public static final long[] ETHEREUM_HASH_POWER_DISTRIBUTION_BIN = {548989, 241491, 31187, 15453, 3204, 578, 23, 1, 1, 1};
    public static final int ETHEREUM_NUM_MINERS_2020 = 56;


    @Override
    public SixRegions sampleMinerRegion() {
        return SixRegions.sixRegionsValues[randomnessEngine.sampleFromDistribution(ETHEREUM_MINER_REGION_DISTRIBUTION_2020)];
    }

    @Override
    public long sampleMinerHashPower() {
        return randomnessEngine.sampleDistributionWithBins(ETHEREUM_HASH_POWER_DISTRIBUTION, ETHEREUM_HASH_POWER_DISTRIBUTION_BIN);
    }

    /**
     * @return 
     */
    @Override
    public int totalNumberOfMiners() {
        return ETHEREUM_NUM_MINERS_2020;
    }
}
