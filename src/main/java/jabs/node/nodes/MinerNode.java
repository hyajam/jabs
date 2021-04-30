package jabs.node.nodes;

public interface MinerNode {
    void generateNewBlock();
    long getHashPower();
}
