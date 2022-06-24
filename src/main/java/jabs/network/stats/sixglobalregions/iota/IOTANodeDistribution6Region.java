package jabs.network.stats.sixglobalregions.iota;

import jabs.network.stats.NodeGlobalRegionDistribution;
import jabs.network.stats.sixglobalregions.GlobalNetworkStats6Region;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class IOTANodeDistribution6Region extends GlobalNetworkStats6Region
        implements NodeGlobalRegionDistribution<SixRegions> {
    private static final double[] IOTA_REGION_DISTRIBUTION_2022 = {0.2549, 0.6078, 0.0392, 0.0588, 0.0393, 0.0000};
    public static final int IOTA_NUM_NODES_2020 = 376;

    public IOTANodeDistribution6Region(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    /**
     * @return 
     */
    @Override
    public SixRegions sampleRegion() {
        return SixRegions.sixRegionsValues[randomnessEngine.sampleFromDistribution(IOTA_REGION_DISTRIBUTION_2022)];
    }

    /**
     * @return 
     */
    @Override
    public int totalNumberOfNodes() {
        return IOTA_NUM_NODES_2020;
    }
}
