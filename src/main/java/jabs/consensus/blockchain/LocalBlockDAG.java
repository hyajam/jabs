package jabs.consensus.blockchain;

import jabs.ledgerdata.Block;

import java.util.*;

/**
 * A DAG graph of blocks received by a node. Each node in the network should
 * have a local DAG of all blocks received. In a Nakamoto Consensus, for example,
 * there is only one main chain and the blocks outside that chain are stale blocks.
 *
 * @param <B> The block type that this DAG supports
 */
public class LocalBlockDAG<B extends Block<B>> {
    /**
     * A map that receives a Block and returns corresponding LocalBlock on local
     * DAG of the node.
     */
    protected final HashMap<B, LocalBlock<B>> localBlockDAG = new HashMap<>();

    /**
     * The genesis block in the local DAG. The block with no parents. This should
     * be set at the first. Since genesis block is fixed for any network node.
     */
    private final LocalBlock<B> genesisBlock;

    /**
     * Creates the local DAG block of a node.
     *
     * @param genesisLocalBlock The block with no parents in the network
     */
    public LocalBlockDAG(B genesisLocalBlock) {
        LocalBlock<B> localGenesisBlock = new LocalBlock<>(genesisLocalBlock);
        localGenesisBlock.isConnectedToGenesis = true;
        this.localBlockDAG.put(genesisLocalBlock, localGenesisBlock);
        this.genesisBlock = localGenesisBlock;
    }

    /**
     * Adds a received block from network to the local DAG block.
     *
     * @param block received block from the network or generated inside
     *              the node itself.
     */
    public void add(B block) {
        if (!this.localBlockDAG.containsKey(block)) {
            LocalBlock<B> localBlock = new LocalBlock<>(block);
            this.localBlockDAG.put(block, localBlock);
            for (B blockInLocalDag: localBlockDAG.keySet()) {
                for (B parentOfBlockInLocalDAG: blockInLocalDag.getParents()) {
                    if (parentOfBlockInLocalDAG == block) {
                        localBlock.children.add(blockInLocalDag);
                    }
                }
            }
            for (B parent: block.getParents()){
                if (this.localBlockDAG.containsKey(parent)) {
                    LocalBlock<B> localParent = this.localBlockDAG.get(parent);
                    localParent.children.add(block);
                    if (localParent.isConnectedToGenesis) {
                        localBlock.isConnectedToGenesis = true;
                        for (B successor: this.getAllSuccessors(block)) {
                            this.localBlockDAG.get(successor).isConnectedToGenesis = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * For any network block returns a local block inside the DAG. If it does not
     * exist in the local DAG then returns null.
     *
     * @param block the network block
     * @return The corresponding local block
     */
    public LocalBlock<B> getLocalBlock(B block) {
        return localBlockDAG.get(block);
    }

    /**
     * Returns total number of blocks inside the local block DAG.
     *
     * @return Number of blocks inside the local block DAG.
     */
    public int size() {
        return localBlockDAG.size();
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
     * Returns all the children of a block in the local block DAG
     *
     * @param block the network block
     * @return A map of all children of the block in local block DAG
     */
    public HashSet<B> getChildren(B block) {
        return this.localBlockDAG.get(block).children;
    }

    /**
     * Checks if a certain network block is available in the local block DAG of the node.
     *
     * @param block the network block to check if it exists in the local block DAG
     * @return true if the block exists in the local DAG, and false if it is not.
     */
    public boolean contains(B block) {
        return this.localBlockDAG.containsKey(block);
    }

    /**
     * Returns all the childless blocks (tips, leaves) that exist of the local block
     * DAG of the node.
     *
     * @return set of all available childless blocks in the local block DAG
     */
    public Set<B> getChildlessBlocks() {
        HashSet<B> childlessBlocks = new HashSet<>();
        for (Map.Entry<B, LocalBlock<B>> block:this.localBlockDAG.entrySet()) {
            if (block.getValue().children.isEmpty()) {
                childlessBlocks.add(block.getKey());
            }
        }
        return childlessBlocks;
    }

    /**
     * Returns set of all blocks in local DAG block that does not have any parents
     * available in the local block DAG.
     *
     * @return set of Dangling blocks or blocks with no parents already received from
     * the network.
     */
    public Set<B> getDanglingBlocks() {
        HashSet<B> danglingBlocks = new HashSet<>();
        for (B block:this.localBlockDAG.keySet()) {
            for (B parent: block.getParents()){
                if (!this.localBlockDAG.containsKey(parent)) {
                    danglingBlocks.add(block);
                }
            }
        }
        return danglingBlocks;
    }

    /**
     * Determines if two blocks are connected to each other or not. If all block between the two
     * input blocks are already available inside the local block DAG then returns true else returns
     * false
     *
     * @param block1 first block
     * @param block2 second block
     * @return true if there is a path between the two blocks and all block between them are
     * available inside the local block DAG else fasle.
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

        HashSet<B> successorsOfOldBlock = getSuccessorsWithHeight(blockOld, blockNew.getHeight());
        return successorsOfOldBlock.contains(blockNew);
    }

    /**
     * Returns all the blocks between two provided blocks inside local block DAG if there is blocks
     * has this property
     *
     * @param block1 first block
     * @param block2 second block
     * @return the list of all blocks between the two input block, starting from the one with higher
     * height block.
     */
    public HashSet<B> getBiconeBetweenBlocks(B block1, B block2) {
        if (!this.localBlockDAG.containsKey(block1) | !this.localBlockDAG.containsKey(block2)) {
            return null;
        }

        if (block1.getHeight() == block2.getHeight()) {
            if (block1 == block2) {
                return new HashSet<>();
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

        HashSet<B> bicone = getAllSuccessorsWithMaxHeight(blockOld, blockNew.getHeight());
        bicone.retainAll(getAllAncestorsWithMinHeight(blockNew, blockOld.getHeight()));

        return bicone;
    }

    /**
     * Returns a hashset of all ancestors of the input block  that are available in the
     * local block DAG
     *
     * @param block the block which its ancestors are requested
     * @return all ancestors in the local block DAG
     */
    public HashSet<B> getAllAncestors(B block) {
        return getAllAncestorsWithMinHeight(block, 0);
    }

    /**
     * Returns a hashset of all ancestors of the input block that are available in the
     * local block DAG and has a height larger than input height
     *
     * @param block the block which its ancestors are requested
     * @param minHeight minimum height of the ancestor blocks
     * @return all ancestors in the local block DAG
     */
    public HashSet<B> getAllAncestorsWithMinHeight(B block, int minHeight) {
        if (!localBlockDAG.containsKey(block)) {
            return new HashSet<>();
        }

        boolean noParentAvailable = true;
        for (B parent: block.getParents()) {
            if (localBlockDAG.containsKey(parent)) {
                noParentAvailable = false;
                break;
            }
        }
        if (noParentAvailable) {
            return new HashSet<>();
        }

        HashSet<B> inCurrentHeight = new HashSet<>();
        HashSet<B> inNextHeight = new HashSet<>();
        HashSet<B> allAncestorsWithMinHeight = new HashSet<>();
        inCurrentHeight.add(block);
        for (int currentHeight = block.getHeight(); currentHeight > minHeight; currentHeight--) {
            for (B ancestor:inCurrentHeight) {
                inNextHeight.addAll(localBlockDAG.get(ancestor).block.getParents());
            }
            inCurrentHeight.clear();
            for (B ancestor: inNextHeight) {
                if (localBlockDAG.containsKey(ancestor)) {
                    inCurrentHeight.add(ancestor);
                }
            }
            inNextHeight.clear();
            for (B ancestor: inCurrentHeight) {
                if (ancestor.getHeight() >= minHeight) {
                    allAncestorsWithMinHeight.add(ancestor);
                }
            }
        }

        return allAncestorsWithMinHeight;
    }

    /**
     * Returns a hashset of all ancestors of the input block with certain height
     * that are available in the local block DAG
     *
     * @param block the block which its ancestors are requested
     * @param height the height of the ancestors
     * @return all ancestors in the local block DAG
     */
    public HashSet<B> getAncestorsWithHeight(B block, int height) {
        HashSet<B> ancestorsWithMaxHeight = getAllSuccessorsWithMaxHeight(block, height);
        HashSet<B> ancestorsWithExactHeight = new HashSet<>();
        for (B ancestor: ancestorsWithMaxHeight) {
            if (ancestor.getHeight() == height){
                ancestorsWithExactHeight.add(ancestor);
            }
        }
        return ancestorsWithExactHeight;
    }

    /**
     * Returns a SortedSet of all available Successors of the input block with
     * certain height inside local block DAG.
     *
     * @param block the block of which the successors are requested
     * @param height the height of the successors
     * @return SortedSet of all available Successors of the input block inside
     * the local block DAG with certain height.
     */
    public HashSet<B> getSuccessorsWithHeight(B block, int height) {
        if (block.getHeight() >= height) {
            return new HashSet<>();
        }

        if (!localBlockDAG.containsKey(block)) {
            return new HashSet<>();
        }

        if (localBlockDAG.get(block).children.isEmpty()) {
            return new HashSet<>();
        }

        HashSet<B> inCurrentHeight = new HashSet<>();
        HashSet<B> inNextHeight = new HashSet<>();
        HashSet<B> allSuccessorsWithHeight = new HashSet<>();
        inCurrentHeight.add(block);
        for (int currentHeight = block.getHeight(); currentHeight <= height; currentHeight++) {
            for (B successor:inCurrentHeight) {
                inNextHeight.addAll(localBlockDAG.get(successor).children);
            }
            inCurrentHeight.clear();
            inCurrentHeight.addAll(inNextHeight);
            for (B successor: inCurrentHeight){
                if (successor.getHeight() == height) {
                    allSuccessorsWithHeight.add(successor);
                }
            }
            inNextHeight.clear();
            if (inCurrentHeight.isEmpty()) {
                return allSuccessorsWithHeight;
            }
        }

        return allSuccessorsWithHeight;
    }

    /**
     * Returns a SortedSet of all available Successors of the input block inside
     * local block DAG
     *
     * @param block the block of which the successors are requested
     * @return SortedSet of all available Successors of the input block inside
     * the local block DAG.
     */
    public HashSet<B> getAllSuccessors(B block) {
        if (!localBlockDAG.containsKey(block)) {
            return new HashSet<>();
        }

        if (localBlockDAG.get(block).children.isEmpty()) {
            return new HashSet<>();
        }

        HashSet<B> inCurrentHeight = new HashSet<>();
        HashSet<B> inNextHeight = new HashSet<>();
        HashSet<B> allSuccessors = new HashSet<>();
        inCurrentHeight.add(block);
        do {
            for (B successor:inCurrentHeight) {
                inNextHeight.addAll(localBlockDAG.get(successor).children);
            }
            inCurrentHeight.clear();
            inCurrentHeight.addAll(inNextHeight);
            allSuccessors.addAll(inCurrentHeight);
            inNextHeight.clear();
        } while (!inCurrentHeight.isEmpty());

        return allSuccessors;
    }


    /**
     * Returns a SortedSet of all available Successors of the input block inside
     * local block DAG that have height below a certain height
     *
     * @param block the block of which the successors are requested.
     * @param height Maximum height
     * @return SortedSet of all available Successors of the input block inside
     * the local block DAG that has a height less than the input value.
     */
    public HashSet<B> getAllSuccessorsWithMaxHeight(B block, int height) {
        if (block.getHeight() >= height) {
            return new HashSet<>();
        }

        if (!localBlockDAG.containsKey(block)) {
            return new HashSet<>();
        }

        if (localBlockDAG.get(block).children.isEmpty()) {
            return new HashSet<>();
        }

        HashSet<B> inCurrentHeight = new HashSet<>();
        HashSet<B> inNextHeight = new HashSet<>();
        HashSet<B> allSuccessorsWithMaxHeight = new HashSet<>();
        inCurrentHeight.add(block);
        for (int currentHeight = block.getHeight(); currentHeight <= height; currentHeight++) {
            for (B successor:inCurrentHeight) {
                inNextHeight.addAll(localBlockDAG.get(successor).children);
            }
            inCurrentHeight.clear();
            inCurrentHeight.addAll(inNextHeight);
            for (B successor: inCurrentHeight){
                if (successor.getHeight() <= height) {
                    allSuccessorsWithMaxHeight.add(successor);
                }
            }
            inNextHeight.clear();
            if (inCurrentHeight.isEmpty()) {
                return allSuccessorsWithMaxHeight;
            }
        }

        return allSuccessorsWithMaxHeight;
    }
}
