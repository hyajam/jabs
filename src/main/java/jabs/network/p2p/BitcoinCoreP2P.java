package jabs.network.p2p;

// TODO: Recheck if it is good to design add neighbor and other functions using events
public class BitcoinCoreP2P extends AbstractBlockchainP2PConnections {
    private static final int BITCOIN_MAX_CONNECTIONS = 125;
    private static final int BITCOIN_NUM_OUTBOUND_CONNECTIONS = 8;

    public BitcoinCoreP2P() {
        super(BITCOIN_NUM_OUTBOUND_CONNECTIONS, BITCOIN_MAX_CONNECTIONS);
    }
    public BitcoinCoreP2P(int numOutboundConnections, int maxConnections) {
        super(numOutboundConnections, maxConnections);
    }
}

