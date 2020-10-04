package main.java.network;

import main.java.config.NetworkStats;
import main.java.node.NodeFactory;
import main.java.node.nodes.Node;

public class NetworkBuilder {
    public static void buildSampleEthereumNetwork(Network network) {
        buildSampleEthereumNetwork(network, NetworkStats.ETHEREUM_NUM_NODES_2020, NetworkStats.ETHEREUM_NUM_MINERS_2020);
    }

    public static void buildSampleEthereumCasperNetwork(Network network, int numOfNonMiner, int numOfMiner, int checkpointSpace) {
        for (int i = 0; i < numOfMiner; i++) {
            network.addMiner(NodeFactory.createNewEthereumCasperMiner(i, checkpointSpace, numOfNonMiner + numOfMiner));
        }

        for (int i = 0; i < numOfNonMiner; i++) {
            network.addNode(NodeFactory.createNewEthereumCasperNode(numOfMiner + i, checkpointSpace, numOfNonMiner+numOfMiner));
        }

        for (Node node:network.getAllNodes()) {
            node.getP2pConnections().connectToNetwork();
        }
    }

    public static void buildSampleEthereumDAGsperNetwork(Network network, int numOfNonMiner, int numOfMiner, int checkpointSpace) {
        for (int i = 0; i < numOfMiner; i++) {
            network.addMiner(NodeFactory.createNewEthereumDAGsperMiner(i, checkpointSpace, numOfNonMiner + numOfMiner));
        }

        for (int i = 0; i < numOfNonMiner; i++) {
            network.addNode(NodeFactory.createNewEthereumDAGsperNode(numOfMiner + i, checkpointSpace, numOfNonMiner+numOfMiner));
        }

        for (Node node:network.getAllNodes()) {
            node.getP2pConnections().connectToNetwork();
        }
    }

    public static void buildSampleEthereumNetwork(Network network, int numOfNonMiner, int numOfMiner) {
        for (int i = 0; i < numOfMiner; i++) {
            network.addMiner(NodeFactory.createNewEthereumMiner(i));
        }

        for (int i = 0; i < numOfNonMiner; i++) {
            network.addNode(NodeFactory.createNewEthereumNode(numOfMiner + i));
        }

        for (Node node:network.getAllNodes()) {
            node.getP2pConnections().connectToNetwork();
        }
    }

    public static void buildSamplePBFTNetwork(Network network, int numOfNodes) {
        for (int i = 0; i < numOfNodes; i++) {
            network.addNode(NodeFactory.createNewPBFTNode(i, numOfNodes));
        }

        for (Node node:network.getAllNodes()) {
            node.getP2pConnections().connectToNetwork();
        }
    }
}
