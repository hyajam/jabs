package jabs.data.pbft;

import jabs.data.Block;
import jabs.node.nodes.Node;

public class PBFTBlock extends Block<PBFTBlock> {
    public static final int PBFT_BLOCK_HASH_SIZE = 32;

    public PBFTBlock(int size, int height, double creationTime, Node creator, PBFTBlock parent) {
        super(size, height, creationTime, creator, parent, PBFT_BLOCK_HASH_SIZE);
    }
}
