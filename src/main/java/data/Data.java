package main.java.data;

public abstract class Data {
    protected int size;

    protected Data(int size) {
        this.size = size;
    }

    public int getSize() { return size; };
}
