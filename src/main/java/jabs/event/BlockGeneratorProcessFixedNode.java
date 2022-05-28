package jabs.event;

import jabs.node.nodes.MinerNode;
import jabs.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

public class BlockGeneratorProcessFixedNode extends AbstractBlockGeneratorProcess {
    public BlockGeneratorProcessFixedNode(Simulator simulator, RandomnessEngine randomnessEngine, MinerNode miner, double averageTimeBetweenBlocks) {
        super(simulator, randomnessEngine, averageTimeBetweenBlocks);
    }

    @Override
    public void generate() {
        simulator.putEvent(new BlockGenerationEvent(miner, simulator), 0);
    }
}
