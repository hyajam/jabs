package jabs.network.stats.sixglobalregions.ethereum;

import jabs.network.stats.NodeGlobalRegionDistribution;
import jabs.network.stats.sixglobalregions.GlobalNetworkStats6Region;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class EthereumNodeGlobalNetworkStats6Regions extends GlobalNetworkStats6Region
        implements NodeGlobalRegionDistribution<SixRegions> {
    private static final double[] ETHEREUM_REGION_DISTRIBUTION_2020 = {0.3503, 0.3563, 0.0100, 0.2358, 0.0414, 0.0062};
    public static final int ETHEREUM_NUM_NODES_2022 = 5906;

    public EthereumNodeGlobalNetworkStats6Regions(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    @Override
    public SixRegions sampleRegion() {
        return SixRegions.sixRegionsValues[randomnessEngine.sampleFromDistribution(ETHEREUM_REGION_DISTRIBUTION_2020)];
    }

    /**
     * @return Number of total nodes in the ethereum network
     */
    @Override
    public int totalNumberOfNodes() {
        return ETHEREUM_NUM_NODES_2022;
    }
}
