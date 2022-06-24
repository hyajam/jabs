package jabs.network.stats.eightysixcountries.ethereum;

import jabs.network.stats.NodeGlobalRegionDistribution;
import jabs.network.stats.eightysixcountries.EightySixCountries;
import jabs.network.stats.sixglobalregions.GlobalNetworkStats6Region;
import jabs.network.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class EthereumNodeGlobalNetworkStats86Countries extends GlobalNetworkStats6Region
        implements NodeGlobalRegionDistribution<EightySixCountries> {

    private static final double[] ETHEREUM_REGION_DISTRIBUTION_2022 = {
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

    public static final int ETHEREUM_NUM_NODES_2022 = 5906;

    public EthereumNodeGlobalNetworkStats86Countries(RandomnessEngine randomnessEngine) {
        super(randomnessEngine);
    }

    @Override
    public EightySixCountries sampleRegion() {
        return EightySixCountries.sixRegionsValues[
                randomnessEngine.sampleFromDistribution(ETHEREUM_REGION_DISTRIBUTION_2022)];
    }

    /**
     * @return Number of total nodes in the ethereum network
     */
    @Override
    public int totalNumberOfNodes() {
        return ETHEREUM_NUM_NODES_2022;
    }
}
