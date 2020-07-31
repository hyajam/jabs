package main.java.node.nodes.bitcoin;

import main.java.node.nodes.MinerNode;

public class BitcoinMinerNode extends BitcoinNode implements MinerNode {
    public BitcoinMinerNode(int nodeID, int region) {
        super(nodeID, region);
    }

    @Override
    public void generateNewBlock() {

    }

    @Override
    public long getHashPower() {
        return 0;
    }
}
