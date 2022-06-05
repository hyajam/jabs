package jabs.network.node.nodes;

public interface MinerNode {
    void generateNewBlock();
    long getHashPower();
}
