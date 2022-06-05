package jabs.simulator.event;

import jabs.network.node.nodes.MinerNode;
import jabs.simulator.randengine.RandomnessEngine;
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
