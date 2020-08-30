package main.java.blockchain;

import main.java.data.Block;

import java.util.*;

public class LocalBlockTree<B extends Block<B>> {
    protected final HashMap<B, LocalBlock<B>> localBlockTree = new HashMap<>();
    private final LocalBlock<B> genesisBlock;

    public LocalBlockTree(B genesisLocalBlock) {
        LocalBlock<B> localGenesisBlock = new LocalBlock<>(genesisLocalBlock);
        localGenesisBlock.isConnectedToGenesis = true;
        this.localBlockTree.put(genesisLocalBlock, localGenesisBlock);
        this.genesisBlock = localGenesisBlock;
    }

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

    public LocalBlock<B> getLocalBlock(B block) {
        return localBlockTree.get(block);
    }

    public int size() {
        return localBlockTree.size();
    }

    public B getGenesisBlock() {
        return this.genesisBlock.block;
    }

    public HashSet<B> getChildren(B block) {
        return this.localBlockTree.get(block).children;
    }

    public boolean contains(B block) {
        return this.localBlockTree.containsKey(block);
    }

    public Set<B> getChildlessBlocks() {
        HashSet<B> childlessBlocks = new HashSet<>();
        for (Map.Entry<B, LocalBlock<B>> block:this.localBlockTree.entrySet()) {
            if (block.getValue().children.isEmpty()) {
                childlessBlocks.add(block.getKey());
            }
        }
        return childlessBlocks;
    }

    public Set<B> getDanglingBlocks() {
        HashSet<B> danglingBlocks = new HashSet<>();
        for (B block:this.localBlockTree.keySet()) {
            if (!this.localBlockTree.containsKey(block.getParent())) {
                danglingBlocks.add(block);
            }
        }
        return danglingBlocks;
    }

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
