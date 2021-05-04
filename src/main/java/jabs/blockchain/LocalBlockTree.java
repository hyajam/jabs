package jabs.blockchain;

import jabs.data.Block;

import java.util.*;

/**
 * A tree graph of blocks received by a node. Each node in the metwrok should
 * have a local tree of all blocks received. In a Nakamoto Consensus, for example,
 * there is only one main chain and the blocks outside that chain are stale blocks.
 *
 * @param <B> The block type that this tree supports
 */
public class LocalBlockTree<B extends Block<B>> {
    /**
     * A map that receives a Block and returns corresponding LocalBlock on local
     * Tree of the node.
     */
    protected final HashMap<B, LocalBlock<B>> localBlockTree = new HashMap<>();

    /**
     * The genesis block in the local tree. The block with no parents. This should
     * be set at the first. Since genesis block is fixed for any network node.
     */
    private final LocalBlock<B> genesisBlock;

    /**
     * Creates the local tree block of a node.
     *
     * @param genesisLocalBlock The block with no parents in the network
     */
    public LocalBlockTree(B genesisLocalBlock) {
        LocalBlock<B> localGenesisBlock = new LocalBlock<>(genesisLocalBlock);
        localGenesisBlock.isConnectedToGenesis = true;
        this.localBlockTree.put(genesisLocalBlock, localGenesisBlock);
        this.genesisBlock = localGenesisBlock;
    }

    /**
     * Adds a received block from network to the local tree block.
     *
     * @param block received block from the network or generated inside
     *              the node itself.
     */
    public void add(B block) {
        if (!this.localBlockTree.containsKey(block)) {
            LocalBlock<B> localBlock = new LocalBlock<>(block);
            this.localBlockTree.put(block, localBlock);
            for (B block1:localBlockTree.keySet()) {
                if (block1.getParent() == block) {
                    localBlock.children.add(block1);
                }
            }
            if (this.localBlockTree.containsKey(block.getParent())) {
                LocalBlock<B> parent = this.localBlockTree.get(block.getParent());
                parent.children.add(block);
                if (parent.isConnectedToGenesis) {
                    localBlock.isConnectedToGenesis = true;
                    for (B successor:this.getAllSuccessors(block)) {
                        this.localBlockTree.get(successor).isConnectedToGenesis = true;
                    }
                }
            }
        }
    }

    /**
     * For any network block returns a local block inside the tree. If it does not
     * exist in the local tree then returns null.
     *
     * @param block the network block
     * @return The corresponding local block
     */
    public LocalBlock<B> getLocalBlock(B block) {
        return localBlockTree.get(block);
    }

    /**
     * Returns total number of blocks inside the local block tree.
     *
     * @return Number of blocks inside the local block tree.
     */
    public int size() {
        return localBlockTree.size();
    }

    /**
     * Returns the block wih no parents that was set in initialization of the node
     *
     * @return genesis block
     */
    public B getGenesisBlock() {
        return this.genesisBlock.block;
    }

    /**
     * Returns all the children of a block in the local block tree
     *
     * @param block the network block
     * @return A map of all children of the block in local block tree
     */
    public HashSet<B> getChildren(B block) {
        return this.localBlockTree.get(block).children;
    }

    /**
     * Checks if a certain network block is available in the local block tree of the node.
     *
     * @param block the network block to check if it exists in the local block tree
     * @return true if the block exists in the local tree, and false if it is not.
     */
    public boolean contains(B block) {
        return this.localBlockTree.containsKey(block);
    }

    /**
     * Returns all of the childless blocks (tips, leaves) that exist of the local block
     * tree of the node.
     *
     * @return set of all available childless blocks in the local block tree
     */
    public Set<B> getChildlessBlocks() {
        HashSet<B> childlessBlocks = new HashSet<>();
        for (Map.Entry<B, LocalBlock<B>> block:this.localBlockTree.entrySet()) {
            if (block.getValue().children.isEmpty()) {
                childlessBlocks.add(block.getKey());
            }
        }
        return childlessBlocks;
    }

    /**
     * Returns set of all blocks in local tree block that does not have any parents
     * available in the local block tree.
     *
     * @return set of Dangling blocks or blocks with no parents already received from
     * the network.
     */
    public Set<B> getDanglingBlocks() {
        HashSet<B> danglingBlocks = new HashSet<>();
        for (B block:this.localBlockTree.keySet()) {
            if (!this.localBlockTree.containsKey(block.getParent())) {
                danglingBlocks.add(block);
            }
        }
        return danglingBlocks;
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
        if (!this.localBlockTree.containsKey(block1) | !this.localBlockTree.containsKey(block2)) {
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
            if (this.localBlockTree.containsKey(ancestor)) {
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
        if (!this.localBlockTree.containsKey(block1) | !this.localBlockTree.containsKey(block2)) {
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
            if (this.localBlockTree.containsKey(ancestor)) {
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
        if (!localBlockTree.containsKey(block.getParent())) {
            return new HashSet<>();
        }

        HashSet<B> ancestors = new HashSet<>();
        B ancestorBlock = block.getParent();
        while (true) {
            if (localBlockTree.containsKey(ancestorBlock)) {
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
    public SortedSet<B> getAllSuccessors(B block) {
        if (localBlockTree.get(block).children.isEmpty()) {
            return new TreeSet<>();
        }

        SortedSet<B> successors = new TreeSet<>();
        SortedSet<B> inCurrentHeight = new TreeSet<>();
        SortedSet<B> inNextHeight = new TreeSet<>();
        inCurrentHeight.add(block);
        do {
            for (B successor:inCurrentHeight) {
                inNextHeight.addAll(localBlockTree.get(successor).children);
            }
            successors.addAll(inNextHeight);
            inCurrentHeight.clear();
            inCurrentHeight.addAll(inNextHeight);
            inNextHeight.clear();
        } while (!inCurrentHeight.isEmpty());

        return successors;
    }
}
