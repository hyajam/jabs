package jabs.network.networks.stats;

public interface NodeGlobalRegionDistribution<R extends Enum<R>> {
    R sampleRegion();

    int totalNumberOfNodes();
}
