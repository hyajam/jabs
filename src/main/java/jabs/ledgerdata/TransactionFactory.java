package jabs.ledgerdata;

import jabs.ledgerdata.bitcoin.BitcoinTx;
import jabs.ledgerdata.ethereum.EthereumTx;
import jabs.simulator.randengine.RandomnessEngine;

public final class TransactionFactory {

    private static final double[] BITCOIN_TRANSACTION_SIZE_DISTRIBUTION = {
            0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0587, 0.4874, 0.2303, 0.1339, 0.0518, 0.0197, 0.0089, 0.0040,
            0.0027, 0.0017, 0.0007, 0.0002
    };

    private static final long[] BITCOIN_TRANSACTION_SIZE_BINS = {
            87, 91, 96, 124, 166, 199, 263, 391, 647, 1159, 2183, 4231, 8327, 16519, 32940, 73141, 270464
    };

    public static BitcoinTx sampleBitcoinTransaction(RandomnessEngine randomnessEngine) {
        return new BitcoinTx((int) randomnessEngine.sampleDistributionWithBins(
                BITCOIN_TRANSACTION_SIZE_DISTRIBUTION, BITCOIN_TRANSACTION_SIZE_BINS));
    }

    private static final double[] ETHEREUM_TRANSACTION_SIZE_DISTRIBUTION = {
            0.33566250, 0.00029251, 0.03196772, 0.00135259, 0.00431051, 0.04577845, 0.46076570, 0.05192406, 0.03867314,
            0.0175110, 0.00219022, 0.00126404, 0.00041145
    };

    private static final long[] ETHEREUM_TRANSACTION_GAS_BINS = {
            21001, 21003, 21007, 21015, 21031, 21063, 21127, 21255, 21511, 22023, 23048, 25097, 29204, 37401, 53785,
            86553, 152114, 283357, 548945, 1141405, 12475587
    };

    private static final double[] BITCOIN_TRANSACTION_GAS_DISTRIBUTION = {
            0.000016894, 0.000069723, 0.000020709, 0.000260152, 0.001366569, 0.000187092, 0.000253752, 0.000358685,
            0.002723765, 0.001816119, 0.003954819, 0.012228619, 0.023134672, 0.139374064, 0.146851576, 0.241159595,
            0.110655111, 0.060103259, 0.032087937, 0.023120530
    };

    private static final long[] ETHEREUM_TRANSACTION_SIZE_BINS = {
            214, 216, 220, 228, 244, 276, 340, 470, 729, 1298, 2639, 5084, 12630, 296985
    };


    public static final long ETHEREUM_MIN_DIFFICULTY = 17146335232L;

    public static EthereumTx sampleEthereumTransaction(RandomnessEngine randomnessEngine) {
        return new EthereumTx(
                (int) randomnessEngine.sampleDistributionWithBins(
                        ETHEREUM_TRANSACTION_SIZE_DISTRIBUTION, ETHEREUM_TRANSACTION_SIZE_BINS),
                (int) randomnessEngine.sampleDistributionWithBins(
                        BITCOIN_TRANSACTION_GAS_DISTRIBUTION, ETHEREUM_TRANSACTION_GAS_BINS));
    }

}
