package jabs.network.stats;

public interface MinerGlobalRegionDistribution<R extends Enum<R>> {
    R sampleMinerRegion();

    long sampleMinerHashPower();

    double shareOfMinersToAllNodes();

    int totalNumberOfMiners();
}
