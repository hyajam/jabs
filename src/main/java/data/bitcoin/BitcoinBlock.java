package main.java.data.bitcoin;

import main.java.data.Block;
import main.java.data.Hash;
import main.java.node.nodes.Node;

import static main.java.network.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinBlock extends Block<BitcoinBlock> {
    public BitcoinBlock(int size, int height, long creationTime, Node creator, BitcoinBlock parent) {
        super(size, height, creationTime, creator, parent, BITCOIN_INV_SIZE);
    }
}
