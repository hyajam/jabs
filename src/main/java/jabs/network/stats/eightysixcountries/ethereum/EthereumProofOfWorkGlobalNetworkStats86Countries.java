package jabs.network.stats.eightysixcountries.ethereum;

import jabs.network.stats.ProofOfWorkGlobalNetworkStats;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.network.stats.sixglobalregions.ethereum.EthereumNodeGlobalNetworkStats6Regions;
import jabs.simulator.randengine.RandomnessEngine;

public class EthereumProofOfWorkGlobalNetworkStats86Countries extends EthereumNodeGlobalNetworkStats6Regions
        implements ProofOfWorkGlobalNetworkStats<SixRegions> {

    public EthereumProofOfWorkGlobalNetworkStats86Countries(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }
    public static final double[] ETHEREUM_MINER_REGION_DISTRIBUTION_2020 = {
            0, 0, 0.00050778605280975, 0.0157413676371022, 0.00626269465132024, 0, 0.0044008124576845,
            0.002708192281652, 0.0010155721056195, 0, 0.0260663507109005, 0.00016926201760325, 0.0206499661475965, 0,
            0.00016926201760325, 0.0023696682464455, 0.00016926201760325, 0.00321597833446175, 0.00050778605280975,
            0.00152335815842925, 0.0401150981719702, 0.0260663507109005, 0.00016926201760325, 0.116621530128639, 0,
            0.000677048070412999, 0, 0, 0.012186865267434, 0.00118483412322275, 0.00220040622884225,
            0.000846310088016249, 0, 0.019634394041977, 0.00220040622884225, 0.00423155044008125, 0.0240352064996615,
            0.000677048070412999, 0, 0, 0.000846310088016249, 0, 0.0023696682464455, 0.0003385240352065,
            0.001354096140826, 0.00050778605280975, 0.000846310088016249, 0.0003385240352065, 0, 0.0154028436018957,
            0.0030467163168585, 0, 0.00186188219363575, 0, 0, 0, 0, 0.00355450236966825, 0.002031144211239,
            0.0016926201760325, 0.011509817197021, 0, 0.00050778605280975, 0.0473933649289099, 0.000677048070412999,
            0.00186188219363575, 0.00050778605280975, 0.0204807041299932, 0.0044008124576845, 0, 0.0023696682464455,
            0.0111712931618145, 0.0044008124576845, 0, 0.00118483412322275, 0, 0.0016926201760325, 0,
            0.0037237643872715, 0.00016926201760325, 0.041299932295193, 0.473087339201083, 0.00050778605280975, 0,
            0.0010155721056195, 0.00152335815842925
    };

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

    public static final long ETHEREUM_DIFFICULTY_2022 = 2097;


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
