package jabs.ledgerdata;

import jabs.ledgerdata.bitcoin.BitcoinBlock;
import jabs.ledgerdata.ethereum.EthereumBlock;
import jabs.ledgerdata.pbft.PBFTBlock;
import jabs.network.node.nodes.bitcoin.BitcoinMinerNode;
import jabs.network.node.nodes.ethereum.EthereumMinerNode;
import jabs.network.node.nodes.pbft.PBFTNode;
import jabs.simulator.randengine.RandomnessEngine;
import jabs.simulator.Simulator;

import java.util.Set;

public final class BlockFactory {
    public static final int ETHEREUM_BLOCK_HEADER_SIZE = 543; // A header could have variable size but mostly its really close this value
    public static final int ETHEREUM_BLOCK_HASH_SIZE = 36; // 32 byte hash + 4 byte network id
    public static final long ETHEREUM_MIN_DIFFICULTY = 17146335232L;

    public static final int BITCOIN_BLOCK_HEADER_SIZE = 80;
    public static final int BITCOIN_INV_SIZE = 36; // 4 byte type + 32 byte hash
    public static final int GET_DATA_OVERHEAD = 4;
    public static final int INV_MESSAGE_OVERHEAD = 1;
    public static final int ETHEREUM_HELLO_MESSAGE_SIZE = 16;

    private static final long[] BITCOIN_BLOCK_SIZE_2020_BINS = {
            196, 119880, 254789, 396047, 553826, 726752, 917631, 1021479, 1054560, 1084003, 1113136, 1138722, 1161695,
            1183942, 1205734, 1227090, 1248408, 1270070, 1293647, 1320186, 1354939, 1423459, 2422858
    };

    private static final double[] BITCOIN_BLOCK_SIZE_2020 = {
            0.0000, 0.0482, 0.0422, 0.0422, 0.0421, 0.0422, 0.0421, 0.0445, 0.0455, 0.0458, 0.0461, 0.0468, 0.0472,
            0.0481, 0.0477, 0.0479, 0.0484, 0.0482, 0.0475, 0.0464, 0.0454, 0.0434, 0.0420
    };

    public static int sampleBitcoinBlockSize(RandomnessEngine randomnessEngine) {
        return (int) randomnessEngine.sampleDistributionWithBins(BITCOIN_BLOCK_SIZE_2020, BITCOIN_BLOCK_SIZE_2020_BINS);
    }

    public static BitcoinBlock sampleBitcoinBlock(Simulator simulator, RandomnessEngine randomnessEngine,
                                                  BitcoinMinerNode creator, BitcoinBlock parent, double difficulty) {
        return new BitcoinBlock(sampleBitcoinBlockSize(randomnessEngine), parent.getHeight() + 1,
                simulator.getCurrentTime(), creator, parent, difficulty);
    }

    public static PBFTBlock samplePBFTBlock(Simulator simulator, RandomnessEngine randomnessEngine, PBFTNode creator, PBFTBlock parent) {
        return new PBFTBlock(sampleBitcoinBlockSize(randomnessEngine), parent.getHeight() + 1,
                simulator.getCurrentTime(), creator, parent); // TODO: Size of PBFT Blocks
    }

    public static EthereumBlock sampleEthereumBlock(Simulator simulator, RandomnessEngine randomnessEngine, EthereumMinerNode creator, EthereumBlock parent,
                                                    Set<EthereumBlock> uncles) {
        return new EthereumBlock(sampleBitcoinBlockSize(randomnessEngine), parent.getHeight() + 1,
                simulator.getCurrentTime(), creator, parent, uncles, ETHEREUM_MIN_DIFFICULTY); // TODO: Block Size
    }
}
