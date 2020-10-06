package main.java.event;

import main.java.node.nodes.MinerNode;
import main.java.simulator.Simulator;

public class BlockGenerationEvent implements Event {
    private final MinerNode miner;
    private final Simulator simulator;

    public BlockGenerationEvent(MinerNode miner, Simulator simulator) {
        this.miner = miner;
        this.simulator = simulator;
    }

    @Override
    public void execute() {
        miner.generateNewBlock();
    }
}
