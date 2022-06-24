package jabs.network.networks.stats;

public interface MinerGlobalRegionDistribution<R extends Enum<R>> {
    R sampleMinerRegion();

    long sampleMinerHashPower();

    int totalNumberOfMiners();
}
