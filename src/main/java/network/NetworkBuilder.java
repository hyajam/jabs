package main.java.network;

import main.java.config.NetworkStats;
import main.java.node.NodeFactory;
import main.java.node.nodes.Node;

import static main.java.network.Network.*;

public class NetworkBuilder {
    public static void buildSampleEthereumNetwork() {
        buildSampleEthereumNetwork(NetworkStats.ETHEREUM_NUM_NODES_2020, NetworkStats.ETHEREUM_NUM_MINERS_2020);
    }

    public static void buildSampleEthereumNetwork(int numOfNonMiner, int numOfMiner) {
        for (int i = 0; i < numOfNonMiner; i++) {
            Network.addNode(NodeFactory.createNewEthereumNode(i));
        }

        for (int i = 0; i < numOfMiner; i++) {
            Network.addMiner(NodeFactory.createNewEthereumMiner(numOfNonMiner + i));
        }

        for (Node node:getAllNodes()) {
            node.getP2pConnections().connectToNetwork();
        }
    }

    public static void buildSamplePBFTNetwork(int numOfNodes) {
        for (int i = 0; i < numOfNodes; i++) {
            Network.addNode(NodeFactory.createNewPBFTNode(i, numOfNodes));
        }

        for (Node node:getAllNodes()) {
            node.getP2pConnections().connectToNetwork();
        }
    }
}
