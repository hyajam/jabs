package main.java.data;

public class BlockHash<B extends Block<B>> extends Hash {
    public BlockHash(int size, Block<B> block) {
        super(size, block);
    }
}
