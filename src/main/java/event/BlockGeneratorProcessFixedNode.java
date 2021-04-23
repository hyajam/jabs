package main.java.event;

import main.java.node.nodes.MinerNode;
import main.java.random.Random;
import main.java.simulator.Simulator;

public class BlockGeneratorProcessFixedNode extends AbstractBlockGeneratorProcess {
    public BlockGeneratorProcessFixedNode(Simulator simulator, Random random, MinerNode miner, long averageTimeBetweenBlocks) {
        super(simulator, random, averageTimeBetweenBlocks);
    }

    @Override
    public void generate() {
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
