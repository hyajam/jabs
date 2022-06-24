package jabs.network.stats.sixglobalregions;

import jabs.network.stats.NetworkStats;
import jabs.simulator.randengine.RandomnessEngine;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static jabs.network.stats.sixglobalregions.SixRegions.*;
import static java.util.Map.entry;

public class GlobalNetworkStats6Region implements NetworkStats<SixRegions> {
    protected final RandomnessEngine randomnessEngine;

    public static final Map<SixRegions, List<Double>> DOWNLOAD_BANDWIDTH_DISTRIBUTION =
            Map.ofEntries(
                    entry(NORTH_AMERICA, Arrays.asList(0.13850, 0.05950, 0.09350, 0.12150, 0.14150, 0.20850, 0.15350, 0.06550, 0.01050, 0.00750)),
                    entry(EUROPE, Arrays.asList(0.10550, 0.06150, 0.08850, 0.14950, 0.19400, 0.17950, 0.13550, 0.06250, 0.02100, 0.00250)),
                    entry(SOUTH_AMERICA, Arrays.asList(0.29650, 0.13400, 0.15350, 0.18700, 0.13700, 0.05800, 0.02550, 0.00850, 0.00000, 0.00000)),
                    entry(ASIA_PACIFIC, Arrays.asList(0.19550, 0.12750, 0.17900, 0.18000, 0.15700, 0.08900, 0.04750, 0.02100, 0.00300, 0.00050)),
                    entry(JAPAN, Arrays.asList(0.25050, 0.10850, 0.14350, 0.11700, 0.09400, 0.11350, 0.06900, 0.09950, 0.00400, 0.00050)),
                    entry(AUSTRALIA, Arrays.asList(0.18850, 0.10500, 0.12300, 0.18200, 0.16400, 0.12750, 0.05100, 0.05800, 0.00100, 0.00000))
            );

    public static final Map<SixRegions, List<Double>> UPLOAD_BANDWIDTH_DISTRIBUTION =
            Map.ofEntries(
                    entry(NORTH_AMERICA, Arrays.asList(0.09750, 0.07350, 0.11550, 0.15750, 0.12200, 0.24000, 0.13800, 0.03850, 0.01100, 0.00650)),
                    entry(EUROPE, Arrays.asList(0.07500, 0.05900, 0.08800, 0.09500, 0.14850, 0.18900, 0.16850, 0.09900, 0.06250, 0.01550)),
                    entry(SOUTH_AMERICA, Arrays.asList(0.21900, 0.17350, 0.16150, 0.10450, 0.18750, 0.12000, 0.02550, 0.00450, 0.00350, 0.00050)),
                    entry(ASIA_PACIFIC, Arrays.asList(0.14350, 0.06850, 0.11150, 0.11800, 0.17050, 0.12500, 0.14200, 0.08000, 0.03650, 0.00450)),
                    entry(JAPAN, Arrays.asList(0.11200, 0.07450, 0.08450, 0.14900, 0.21600, 0.23100, 0.10250, 0.02500, 0.00550, 0.00000)),
                    entry(AUSTRALIA, Arrays.asList(0.09650, 0.06950, 0.31000, 0.09550, 0.12600, 0.12650, 0.03000, 0.09850, 0.04550, 0.00200))
            );

    /**
     * Sample download speed in bits per second
     */
    public static final long[] DOWNLOAD_BANDWIDTH_BIN = {
            600000, 1600000, 3600000, 7600000, 15600000, 31600000, 63600000, 127600000, 255600000, 499600000
    };


    /**
     * Sample upload speed in bits per second
     */
    public static final long[] UPLOAD_BANDWIDTH_BIN = {
            200000,  400000,  800000, 1600000,  3200000,  6400000, 12800000,  25600000,  51200000,  99900000
    };

    // Unit: seconds
    protected static final Map<SixRegions, Map<SixRegions, Double>> GLOBAL_LATENCY_BY_REGION =
            Map.ofEntries(
                    entry(NORTH_AMERICA,
                            Map.ofEntries(
                                entry(NORTH_AMERICA, 0.032),
                                entry(EUROPE, 0.124),
                                entry(SOUTH_AMERICA, 0.184),
                                entry(ASIA_PACIFIC, 0.198),
                                entry(JAPAN, 0.151),
                                entry(AUSTRALIA, 0.189)
                            )
                    ),
                    entry(EUROPE,
                            Map.ofEntries(
                                entry(NORTH_AMERICA, 0.124),
                                entry(EUROPE, 0.011),
                                entry(SOUTH_AMERICA, 0.227),
                                entry(ASIA_PACIFIC, 0.237),
                                entry(JAPAN, 0.252),
                                entry(AUSTRALIA, 0.294)
                            )
                    ),
                    entry(SOUTH_AMERICA,
                            Map.ofEntries(
                                entry(NORTH_AMERICA, 0.184),
                                entry(EUROPE, 0.227),
                                entry(SOUTH_AMERICA, 0.088),
                                entry(ASIA_PACIFIC, 0.325),
                                entry(JAPAN, 0.301),
                                entry(AUSTRALIA, 0.322)
                            )
                    ),
                    entry(ASIA_PACIFIC,
                            Map.ofEntries(
                                entry(NORTH_AMERICA, 0.198),
                                entry(EUROPE, 0.237),
                                entry(SOUTH_AMERICA, 0.325),
                                entry(ASIA_PACIFIC, 0.085),
                                entry(JAPAN, 0.058),
                                entry(AUSTRALIA, 0.198)
                            )
                    ),
                    entry(JAPAN,
                            Map.ofEntries(
                                    entry(NORTH_AMERICA, 0.151),
                                    entry(EUROPE, 0.252),
                                    entry(SOUTH_AMERICA, 0.301),
                                    entry(ASIA_PACIFIC, 0.058),
                                    entry(JAPAN, 0.012),
                                    entry(AUSTRALIA, 0.126)
                            )
                    ),
                    entry(AUSTRALIA,
                            Map.ofEntries(
                                    entry(NORTH_AMERICA, 0.189),
                                    entry(EUROPE, 0.294),
                                    entry(SOUTH_AMERICA, 0.322),
                                    entry(ASIA_PACIFIC, 0.198),
                                    entry(JAPAN, 0.126),
                                    entry(AUSTRALIA, 0.016)
                            )
                    )
            );

    public static final double LATENCY_PARETO_SHAPE = 5;

    public GlobalNetworkStats6Region(RandomnessEngine randomnessEngine) {
        this.randomnessEngine = randomnessEngine;
    }

    @Override
    public double getLatency(SixRegions fromPosition, SixRegions toPosition) {
        double mean = GLOBAL_LATENCY_BY_REGION.get(fromPosition).get(toPosition);
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * mean;
        return randomnessEngine.sampleParetoDistribution(scale, LATENCY_PARETO_SHAPE);
    }

    private long sampleBandwidthByRegion(SixRegions region, Map<SixRegions, List<Double>> dist, long[] bins) {
        return randomnessEngine.sampleDistributionWithBins(dist.get(region), bins);
    }

    @Override
    public long sampleDownloadBandwidth(SixRegions region) {
        return sampleBandwidthByRegion(region, DOWNLOAD_BANDWIDTH_DISTRIBUTION, DOWNLOAD_BANDWIDTH_BIN);
    }

    @Override
    public long sampleUploadBandwidth(SixRegions region) {
        return sampleBandwidthByRegion(region, UPLOAD_BANDWIDTH_DISTRIBUTION, UPLOAD_BANDWIDTH_BIN);
    }
}
