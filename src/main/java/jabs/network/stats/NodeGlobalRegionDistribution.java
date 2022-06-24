package jabs.network.stats;

public interface NodeGlobalRegionDistribution<R extends Enum<R>> {
    R sampleRegion();

    int totalNumberOfNodes();
}
