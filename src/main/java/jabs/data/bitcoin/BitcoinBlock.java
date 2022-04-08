package jabs.data.bitcoin;

import jabs.data.Block;
import jabs.node.nodes.Node;

import static jabs.network.BlockFactory.BITCOIN_INV_SIZE;

public class BitcoinBlock extends Block<BitcoinBlock> {
    public BitcoinBlock(int size, int height, double creationTime, Node creator, BitcoinBlock parent) {
        super(size, height, creationTime, creator, parent, BITCOIN_INV_SIZE);
    }
}
