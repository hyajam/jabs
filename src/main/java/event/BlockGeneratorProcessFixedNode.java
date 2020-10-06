package main.java.event;

import main.java.node.nodes.MinerNode;
import main.java.simulator.Simulator;

public class BlockGeneratorProcessFixedNode extends AbstractBlockGeneratorProcess {
    public BlockGeneratorProcessFixedNode(Simulator simulator,MinerNode miner, long averageTimeBetweenBlocks) {
        super(simulator, averageTimeBetweenBlocks);
    }

    @Override
    public void generate() {
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
