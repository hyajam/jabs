package main.java.event;

import main.java.node.nodes.MinerNode;
import main.java.simulator.Simulator;

public class BlockGeneratorProcessFixedNode extends AbstractBlockGeneratorProcess {
    public BlockGeneratorProcessFixedNode(MinerNode miner, long averageTimeBetweenBlocks) {
        super(averageTimeBetweenBlocks);
    }

    @Override
    public void generate() {
        Simulator.putEvent(new BlockGenerationEvent(miner), 0);
    }
}
