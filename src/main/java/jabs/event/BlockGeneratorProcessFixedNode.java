package jabs.event;

import jabs.node.nodes.MinerNode;
import jabs.random.Random;
import jabs.simulator.Simulator;

public class BlockGeneratorProcessFixedNode extends AbstractBlockGeneratorProcess {
    public BlockGeneratorProcessFixedNode(Simulator simulator, Random random, MinerNode miner, long averageTimeBetweenBlocks) {
        super(simulator, random, averageTimeBetweenBlocks);
    }

    @Override
    public void generate() {
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
