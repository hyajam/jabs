package main.java.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class NetworkStats {
    public static final List<String> REGION_LIST = new ArrayList<>(Arrays.asList("NORTH_AMERICA", "EUROPE", "SOUTH_AMERICA", "ASIA_PACIFIC", "JAPAN", "AUSTRALIA"));

    public static final double[][] DOWNLOAD_BANDWIDTH_DISTRIBUTION = {
            {0.13850, 0.05950, 0.09350, 0.12150, 0.14150, 0.20850, 0.15350, 0.06550, 0.01050, 0.00750},
            {0.10550, 0.06150, 0.08850, 0.14950, 0.19400, 0.17950, 0.13550, 0.06250, 0.02100, 0.00250},
            {0.29650, 0.13400, 0.15350, 0.18700, 0.13700, 0.05800, 0.02550, 0.00850, 0.00000, 0.00000},
            {0.19550, 0.12750, 0.17900, 0.18000, 0.15700, 0.08900, 0.04750, 0.02100, 0.00300, 0.00050},
            {0.25050, 0.10850, 0.14350, 0.11700, 0.09400, 0.11350, 0.06900, 0.09950, 0.00400, 0.00050},
            {0.18850, 0.10500, 0.12300, 0.18200, 0.16400, 0.12750, 0.05100, 0.05800, 0.00100, 0.00000},
    };

    public static final double[][] UPLOAD_BANDWIDTH_DISTRIBUTION = {
            {0.09750, 0.07350, 0.11550, 0.15750, 0.12200, 0.24000, 0.13800, 0.03850, 0.01100, 0.00650},
            {0.07500, 0.05900, 0.08800, 0.09500, 0.14850, 0.18900, 0.16850, 0.09900, 0.06250, 0.01550},
            {0.21900, 0.17350, 0.16150, 0.10450, 0.18750, 0.12000, 0.02550, 0.00450, 0.00350, 0.00050},
            {0.14350, 0.06850, 0.11150, 0.11800, 0.17050, 0.12500, 0.14200, 0.08000, 0.03650, 0.00450},
            {0.11200, 0.07450, 0.08450, 0.14900, 0.21600, 0.23100, 0.10250, 0.02500, 0.00550, 0.00000},
            {0.09650, 0.06950, 0.31000, 0.09550, 0.12600, 0.12650, 0.03000, 0.09850, 0.04550, 0.00200},
    };

    public static final long[] DOWNLOAD_BANDWIDTH_BIN = {
            600000, 1600000, 3600000, 7600000, 15600000, 31600000, 63600000, 127600000, 255600000, 499600000
    };

    public static final long[] UPLOAD_BANDWIDTH_BIN = {
            200000,  400000,  800000, 1600000,  3200000,  6400000, 12800000,  25600000,  51200000,  99900000
    };

    // LATENCY[i][j] is average latency from REGION_LIST[i] to REGION_LIST[j]
    // Unit: millisecond
    public static final long[][] LATENCY = {
            { 32, 124, 184, 198, 151, 189},
            {124,  11, 227, 237, 252, 294},
            {184, 227,  88, 325, 301, 322},
            {198, 237, 325,  85,  58, 198},
            {151, 252, 301,  58,  12, 126},
            {189, 294, 322, 198, 126,  16}
    };

    public static final double LATENCY_PARETO_SHAPE = 5;

    public static final double[] BITCOIN_REGION_DISTRIBUTION_2019 = {0.3316, 0.4998, 0.0090, 0.1177, 0.0224, 0.0195};
    public static final double[] ETHEREUM_REGION_DISTRIBUTION_2020 = {0.3503, 0.3563, 0.0100, 0.2358, 0.0414, 0.0062};

    public static final double[] MINER_REGION_DISTRIBUTION_BITCOIN_2020 = {0.0875, 0.0863, 0.0111, 0.8146, 0.0000, 0.0005};

    // public static final long[] ETHEREUM_DEGREE_DISTRIBUTION_2020_BIN = {1, 2, 7, 12, 13, 14, 17, 50, 58, 100, 106, 838};
    // public static final double[] ETHEREUM_DEGREE_DISTRIBUTION_2020 = {0.0204, 0.0204, 0.0204, 0.3673, 0.1224, 0.0408, 0.0816, 0.2451, 0.0204, 0.0204, 0.0204, 0.0204};

    // public static final long[] BITCOIN_DEGREE_DISTRIBUTION_2015_BIN = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
    // public static final double[] BITCOIN_DEGREE_DISTRIBUTION_2015 = {0.025, 0.025, 0.025, 0.025, 0.100, 0.100, 0.100, 0.100, 0.100, 0.100, 0.100, 0.050, 0.050, 0.050, 0.020, 0.000, 0.010, 0.010, 0.005, 0.005};

    public static final double[] ETHEREUM_HASH_POWER_DISTRIBUTION = {0.0035, 0.0035, 0.0070, 0.0140, 0.0279, 0.0558, 0.1117, 0.2234, 0.4468, 0.1065};
    public static final long[] ETHEREUM_HASH_POWER_DISTRIBUTION_BIN = {548989, 241491, 31187, 15453, 3204, 578, 23, 1, 1, 1};

    public static final int ETHEREUM_NUM_NODES_2020 = 6203;
    public static final int ETHEREUM_NUM_MINERS_2020 = 56;
}
