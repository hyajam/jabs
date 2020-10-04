package main.java.network;

public class LANNetwork extends Network {
    @Override
    public long getLatency(int from, int to) {
        return 0;
    }

    @Override
    public long sampleDownloadBandwidth(int region) {
        return 0;
    }

    @Override
    public long sampleUploadBandwidth(int region) {
        return 0;
    }
}
