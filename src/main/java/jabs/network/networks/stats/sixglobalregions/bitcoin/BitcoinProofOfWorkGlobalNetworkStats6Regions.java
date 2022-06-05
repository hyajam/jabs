package jabs.network.networks.stats.sixglobalregions.bitcoin;

import jabs.network.networks.stats.MinerGlobalRegionDistribution;
import jabs.network.networks.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.networks.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class BitcoinProofOfWorkGlobalNetworkStats6Regions extends BitcoinNodeGlobalNetworkStats6Regions
        implements ProofOfWorkGlobalNetworkStats<SixRegions> {

    public BitcoinProofOfWorkGlobalNetworkStats6Regions(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }


    public static final double[] BITCOIN_MINER_REGION_DISTRIBUTION_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    /**
     * Hash power probability distribution (CDF) in Bitcoin Network
     */
    public static final double[] BITCOIN_HASH_POWER_DISTRIBUTION = {0.0035, 0.0035, 0.0070, 0.0140, 0.0279, 0.0558, 0.1117, 0.2234, 0.4468, 0.1065};

    /**
     * Hash power probability distribution (Hash Power Values) in Bitcoin Network
     */
    public static final long[] BITCOIN_HASH_POWER_DISTRIBUTION_BIN = {548989, 241491, 31187, 15453, 3204, 578, 23, 1, 1, 1};

    public static final int BITCOIN_NUM_MINERS_2022 = 30;

    @Override
    public SixRegions sampleMinerRegion() {
        return SixRegions.sixRegionsValues[randomnessEngine.sampleFromDistribution(BITCOIN_MINER_REGION_DISTRIBUTION_2020)];
    }

    @Override
    public long sampleMinerHashPower() {
        return randomnessEngine.sampleDistributionWithBins(BITCOIN_HASH_POWER_DISTRIBUTION, BITCOIN_HASH_POWER_DISTRIBUTION_BIN);
    }

    @Override
    public int totalNumberOfMiners() {
        return BITCOIN_NUM_MINERS_2022;
    }
}
