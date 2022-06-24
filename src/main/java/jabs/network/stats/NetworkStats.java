package jabs.network.stats;

public interface NetworkStats<R extends Enum<R>> {

    double getLatency(R fromPosition, R toPosition);

    long sampleDownloadBandwidth(R position);

    long sampleUploadBandwidth(R position);
}
