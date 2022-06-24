package jabs.network.networks.stats.lan;

import jabs.network.networks.stats.NetworkStats;
import jabs.network.networks.stats.sixglobalregions.SixRegions;
import jabs.simulator.randengine.RandomnessEngine;

public class LAN100MNetworkStats implements NetworkStats<SingleNodeType> {
    protected final RandomnessEngine randomnessEngine;
    protected static final double LAN_AVERAGE_LATENCY = 0.002; // 2 milli seconds
    protected static final double LATENCY_PARETO_SHAPE = 5;
    protected static final long LAN_AVERAGE_BANDWIDTH = 100000000;
    

    public LAN100MNetworkStats(RandomnessEngine randomnessEngine) {
        this.randomnessEngine = randomnessEngine;
    }

    /**
     * @param fromPosition 
     * @param toPosition
     * @return
     */
    @Override
    public double getLatency(SingleNodeType fromPosition, SingleNodeType toPosition) {
        double scale = ((LATENCY_PARETO_SHAPE-1)/LATENCY_PARETO_SHAPE) * LAN_AVERAGE_LATENCY;
        return randomnessEngine.sampleParetoDistribution(scale, LATENCY_PARETO_SHAPE);
    }

    /**
     * @param position 
     * @return
     */
    @Override
    public long sampleDownloadBandwidth(SingleNodeType position) {
        return LAN_AVERAGE_BANDWIDTH;
    }

    /**
     * @param position 
     * @return
     */
    @Override
    public long sampleUploadBandwidth(SingleNodeType position) {
        return LAN_AVERAGE_BANDWIDTH;
    }
}
