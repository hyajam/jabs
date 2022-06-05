package jabs.simulator.event;

import jabs.network.node.nodes.MinerNode;
import jabs.simulator.Simulator;

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
