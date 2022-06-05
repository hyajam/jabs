package jabs.consensus.blockchain;

import jabs.ledgerdata.SingleParentBlock;

import java.util.*;

/**
 * A tree graph of blocks received by a node. Each node in the metwrok should
 * have a local tree of all blocks received. In a Nakamoto Consensus, for example,
 * there is only one main chain and the blocks outside that chain are stale blocks.
 *
 * @param <B> The block type that this tree supports
 */
public class LocalBlockTree<B extends SingleParentBlock<B>> extends LocalBlockDAG<B> {
    /**
     * Creates the local tree block of a node.
     *
     * @param genesisLocalBlock The block with no parents in the network
     */
    public LocalBlockTree(B genesisLocalBlock) {
        super(genesisLocalBlock);
    }

    /**
     * Adds a received block from network to the local tree block.
     *
     * @param block received block from the network or generated inside
     *              the node itself.
     */
    public void add(B block) {
        if (!this.localBlockDAG.containsKey(block)) {
            LocalBlock<B> localBlock = new LocalBlock<>(block);
            this.localBlockDAG.put(block, localBlock);
            for (B block1:localBlockDAG.keySet()) {
                if (block1.getParent() == block) {
                    localBlock.children.add(block1);
                }
            }
            if (this.localBlockDAG.containsKey(block.getParent())) {
                LocalBlock<B> parent = this.localBlockDAG.get(block.getParent());
                parent.children.add(block);
                if (parent.isConnectedToGenesis) {
                    localBlock.isConnectedToGenesis = true;
                    for (B successor:this.getAllSuccessors(block)) {
                        this.localBlockDAG.get(successor).isConnectedToGenesis = true;
                    }
                }
            }
        }
    }

    /**
     * Determines if two blocks are connected to each other or not. If all block between the two
     * input blocks are already available inside the local block tree then returns true else returns
     * false
     *
     * @param block1 first block
     * @param block2 second block
     * @return true if there is a path between the two blocks and all block between them are
     * available inside the local block tree else fasle.
     */
    public boolean areBlocksConnected(B block1, B block2) {
        if (!this.localBlockDAG.containsKey(block1) | !this.localBlockDAG.containsKey(block2)) {
            return false;
        }

        if (block1.getHeight() == block2.getHeight()) {
            return block1 == block2;
        }

        B blockNew = block1;
        B blockOld = block2;
        if (block1.getHeight() < block2.getHeight()) {
            blockNew = block2;
            blockOld = block1;
        }

        B ancestor = blockNew.getParent();
        while (true) {
            if (this.localBlockDAG.containsKey(ancestor)) {
                if (ancestor.getHeight() == blockOld.getHeight()) {
                    return ancestor == blockOld;
                }
            } else {
                return false;
            }
            ancestor = ancestor.getParent();
        }
    }

    /**
     * Returns all the blocks between two provided blocks inside local block tree if there is any
     * such path between them.
     *
     * @param block1 first block
     * @param block2 second block
     * @return the list of all blocks between the two input block, starting from the one with higher
     * height block.
     */
    public List<B> getPathBetween(B block1, B block2) {
        if (!this.localBlockDAG.containsKey(block1) | !this.localBlockDAG.containsKey(block2)) {
            return null;
        }

        if (block1.getHeight() == block2.getHeight()) {
            if (block1 == block2) {
                return Collections.emptyList();
            } else {
                return null;
            }
        }

        B blockNew = block1;
        B blockOld = block2;
        if (block1.getHeight() < block2.getHeight()) {
            blockNew = block2;
            blockOld = block1;
        }

        List<B> path = new ArrayList<>();
        path.add(blockNew);

        B ancestor = blockNew.getParent();
        while (true) {
            if (this.localBlockDAG.containsKey(ancestor)) {
                if (ancestor.getHeight() == blockOld.getHeight()) {
                    if (ancestor == blockOld) {
                        return path;
                    } else {
                        return null;
                    }
                }
                path.add(ancestor);
            } else {
                return null;
            }
            ancestor = ancestor.getParent();
        }
    }

    /**
     * Returns the highest common ancestor of the provided two blocks.
     *
     * @param blockA First block
     * @param blockB Second block
     * @return The ancestor with largest height value.
     */
    public B getCommonAncestor(B blockA, B blockB) {
        // these are going to be same height blocks which are ancestors of A and B
        B blockX = blockA;
        B blockY = blockB;

        if (blockA.getHeight() > blockB.getHeight()) {
            blockX = getAncestorOfHeight(blockA, blockB.getHeight());
        } else if (blockA.getHeight() < blockB.getHeight()) {
            blockY = getAncestorOfHeight(blockB, blockA.getHeight());;
        }

        while (true) {
            if (blockX == blockY) {
                return blockX;
            } else {
                blockX = blockX.getParent();
                blockY = blockY.getParent();
            }
        }
    }

    /**
     * Returns the ancestor of the block with at certain height.
     *
     * @param block the block
     * @param height the targeted height that the returning ancestor is expected to have
     * @return the ancestor with height equal to the input height.
     */
    public B getAncestorOfHeight(B block, int height) {
        if (block.getHeight() == height) {
            return block;
        } else if (block.getHeight() < height) {
            return null;
        } else {
            B ancestorBlock = block.getParent();
            while (true) {
                if (ancestorBlock.getHeight() == height) {
                    return ancestorBlock;
                }
                ancestorBlock = ancestorBlock.getParent();
            }
        }
    }

    /**
     * Returns all ancestors of the input block in a HashSet
     *
     * @param block the block which its ancestors are requested
     * @return all ancestors in the local block tree
     */
    public HashSet<B> getAllAncestors(B block) {
        if (!localBlockDAG.containsKey(block.getParent())) {
            return new HashSet<>();
        }

        HashSet<B> ancestors = new HashSet<>();
        B ancestorBlock = block.getParent();
        while (true) {
            if (localBlockDAG.containsKey(ancestorBlock)) {
                ancestors.add(ancestorBlock);
            } else {
                return ancestors;
            }
            ancestorBlock = ancestorBlock.getParent();
        }
    }

    /**
     * Returns a SortedSet of all available Successors of the input block inside
     * local block tree
     *
     * @param block the block of which the successors are requested
     * @return SortedSet of all available Successors of the input block inside
     * the local block tree.
     */
    public HashSet<B> getAllSuccessors(B block) {
        if (localBlockDAG.get(block).children.isEmpty()) {
            return new HashSet<>();
        }

        HashSet<B> successors = new HashSet<>();
        HashSet<B> inCurrentHeight = new HashSet<>();
        HashSet<B> inNextHeight = new HashSet<>();
        inCurrentHeight.add(block);
        do {
            for (B successor:inCurrentHeight) {
                inNextHeight.addAll(localBlockDAG.get(successor).children);
            }
            successors.addAll(inNextHeight);
            inCurrentHeight.clear();
            inCurrentHeight.addAll(inNextHeight);
            inNextHeight.clear();
        } while (!inCurrentHeight.isEmpty());

        return successors;
    }
}
