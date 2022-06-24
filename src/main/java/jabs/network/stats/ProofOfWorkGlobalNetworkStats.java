package jabs.network.stats;

public interface ProofOfWorkGlobalNetworkStats<R extends Enum<R>> extends NetworkStats<R>,
        NodeGlobalNetworkStats<R>, MinerGlobalRegionDistribution<R> {
}
