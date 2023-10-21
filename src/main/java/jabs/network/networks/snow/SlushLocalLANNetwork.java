package jabs.network.networks.snow;

import jabs.consensus.config.ConsensusAlgorithmConfig;
import jabs.network.networks.Network;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.snow.SlushNode;
import jabs.network.stats.lan.LAN100MNetworkStats;
import jabs.network.stats.lan.SingleNodeType;
import jabs.simulator.Simulator;
import jabs.simulator.randengine.RandomnessEngine;

public class SlushLocalLANNetwork extends Network<SlushNode, SingleNodeType> {
  public SlushLocalLANNetwork(RandomnessEngine randomnessEngine) {
    super(randomnessEngine, new LAN100MNetworkStats(randomnessEngine));
  }

  public SlushNode createNewSlushNode(Simulator simulator, int nodeID, int numAllParticipants) {
    return new SlushNode(simulator, this, nodeID,
        this.sampleDownloadBandwidth(SingleNodeType.LAN_NODE),
        this.sampleUploadBandwidth(SingleNodeType.LAN_NODE),
        numAllParticipants);
  }

  @Override
  public void populateNetwork(Simulator simulator, ConsensusAlgorithmConfig snowConsensusConfig) {
    populateNetwork(simulator, 40, snowConsensusConfig);
  }

  @Override
  public void populateNetwork(Simulator simulator, int numNodes, ConsensusAlgorithmConfig SLushConsensusConfig) {
    for (int i = 0; i < numNodes; i++) {
      this.addNode(createNewSlushNode(simulator, i, numNodes), SingleNodeType.LAN_NODE);
    }

    for (Node node : this.getAllNodes()) {
      node.getP2pConnections().connectToNetwork(this);
    }
  }

  /**
   * @param node A SLush node to add to the network
   */
  @Override
  public void addNode(SlushNode node) {
    this.addNode(node, SingleNodeType.LAN_NODE);
  }
}
