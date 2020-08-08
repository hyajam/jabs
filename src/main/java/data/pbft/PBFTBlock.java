package main.java.data.pbft;

import main.java.data.Block;
import main.java.node.nodes.Node;

public class PBFTBlock extends Block<PBFTBlock> {
    public static final int PBFT_BLOCK_HASH_SIZE = 32;

    public PBFTBlock(int size, int height, long creationTime, Node creator, PBFTBlock parent) {
        super(size, height, creationTime, creator, parent, PBFT_BLOCK_HASH_SIZE);
    }
}
