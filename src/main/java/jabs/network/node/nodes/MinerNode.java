package jabs.network.node.nodes;

public interface MinerNode {
    void generateNewBlock();

    void startMining();

    void stopMining();

    double getHashPower();
}
