package jabs.network.stats.sixglobalregions.bitcoin;

import jabs.network.stats.NodeGlobalRegionDistribution;
import jabs.network.stats.sixglobalregions.GlobalNetworkStats6Region;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class BitcoinNodeGlobalNetworkStats6Regions extends GlobalNetworkStats6Region
        implements NodeGlobalRegionDistribution<SixRegions> {
    private static final double[] BITCOIN_REGION_DISTRIBUTION_2019 = {0.3316, 0.4998, 0.0090, 0.1177, 0.0224, 0.0195};
    public static final int BITCOIN_NUM_NODES_2022 = 16052;

    public BitcoinNodeGlobalNetworkStats6Regions(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    @Override
    public SixRegions sampleRegion() {
        return SixRegions.sixRegionsValues[randomnessEngine.sampleFromDistribution(BITCOIN_REGION_DISTRIBUTION_2019)];
    }

    @Override
    public int totalNumberOfNodes() {
        return BITCOIN_NUM_NODES_2022;
    }
}
