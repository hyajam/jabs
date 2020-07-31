package main.java.network;

import main.java.data.bitcoin.BitcoinTx;
import main.java.data.ethereum.EthereumTx;

import static main.java.random.RandomSampling.sampleDistributionWithBins;

public final class TransactionFactory {
    private static final double[] ETHEREUM_TRANSACTION_SIZE_DISTRIBUTION = {
            0.33566250, 0.00029251, 0.03196772, 0.00135259, 0.00431051, 0.04577845, 0.46076570, 0.05192406, 0.03867314,
            0.0175110, 0.00219022, 0.00126404, 0.00041145
    };

    private static final long[] ETHEREUM_TRANSACTION_SIZE_BINS = {
            214, 216, 220, 228, 244, 276, 340, 470, 729, 1298, 2639, 5084, 12630, 296985
    };

    private static final double[] BITCOIN_TRANSACTION_SIZE_DISTRIBUTION = {
            0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0587, 0.4874, 0.2303, 0.1339, 0.0518, 0.0197, 0.0089, 0.0040,
            0.0027, 0.0017, 0.0007, 0.0002
    };

    private static final long[] BITCOIN_TRANSACTION_SIZE_BINS = {
            87, 91, 96, 124, 166, 199, 263, 391, 647, 1159, 2183, 4231, 8327, 16519, 32940, 73141, 270464
    };

    public static EthereumTx sampleEthereumTransaction() {
        return new EthereumTx((int) sampleDistributionWithBins(
                ETHEREUM_TRANSACTION_SIZE_DISTRIBUTION, ETHEREUM_TRANSACTION_SIZE_BINS));
    }

    public static BitcoinTx sampleBitcoinTransaction() {
        return new BitcoinTx((int) sampleDistributionWithBins(
                BITCOIN_TRANSACTION_SIZE_DISTRIBUTION, BITCOIN_TRANSACTION_SIZE_BINS));
    }
}
