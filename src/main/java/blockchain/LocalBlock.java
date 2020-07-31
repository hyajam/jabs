package main.java.blockchain;

import main.java.data.Block;

import java.util.HashSet;

public class LocalBlock<B extends Block<B>> {
    final public B block;
    final public HashSet<B> children;
    public boolean isConnectedToGenesis = false;

    public LocalBlock(B block) {
        this.block = block;
        this.children = new HashSet<>();
    }
}
