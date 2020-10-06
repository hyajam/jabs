package main.java.network;

import main.java.data.bitcoin.BitcoinTx;
import main.java.random.Random;

public final class TransactionFactory {
    private static final double[] BITCOIN_TRANSACTION_SIZE_DISTRIBUTION = {
            0.0000, 0.0000, 0.0000, 0.0000, 0.0000, 0.0587, 0.4874, 0.2303, 0.1339, 0.0518, 0.0197, 0.0089, 0.0040,
            0.0027, 0.0017, 0.0007, 0.0002
    };

    private static final long[] BITCOIN_TRANSACTION_SIZE_BINS = {
            87, 91, 96, 124, 166, 199, 263, 391, 647, 1159, 2183, 4231, 8327, 16519, 32940, 73141, 270464
    };

    public static BitcoinTx sampleBitcoinTransaction(Random random) {
        return new BitcoinTx((int) random.sampleDistributionWithBins(
                BITCOIN_TRANSACTION_SIZE_DISTRIBUTION, BITCOIN_TRANSACTION_SIZE_BINS));
    }
}
