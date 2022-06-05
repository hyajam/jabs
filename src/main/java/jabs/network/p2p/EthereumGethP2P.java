package jabs.network.p2p;

public class EthereumGethP2P extends AbstractBlockchainP2PConnections {
    private static final int ETHEREUM_MAX_PEER_COUNT = 10;
    private static final int ETHEREUM_DIAL_RATIO = 3;

    public EthereumGethP2P() {
        super((ETHEREUM_MAX_PEER_COUNT / ETHEREUM_DIAL_RATIO),
                ETHEREUM_MAX_PEER_COUNT);
    }
    public EthereumGethP2P(int maxPeerCount, int dialRatio) {
        super((maxPeerCount / dialRatio), maxPeerCount);
    }
}

