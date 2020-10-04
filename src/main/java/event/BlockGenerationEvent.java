package main.java.event;

import main.java.node.nodes.MinerNode;

public class BlockGenerationEvent implements Event {
    private final MinerNode miner;

    public BlockGenerationEvent(MinerNode miner) {
        this.miner = miner;
    }

    @Override
    public void execute() {
        miner.generateNewBlock();
    }
}
