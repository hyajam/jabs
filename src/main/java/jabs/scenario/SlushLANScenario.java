package jabs.scenario;

import jabs.consensus.algorithm.Snow;
import jabs.consensus.config.SnowConsensusConfig;
import jabs.ledgerdata.BlockFactory;
import jabs.ledgerdata.snow.SnowBlock;
import jabs.ledgerdata.snow.SnowPrepareQuery;
import jabs.network.message.QueryMessage;
import jabs.network.networks.snow.SlushLocalLANNetwork;
import jabs.network.node.nodes.Node;
import jabs.network.node.nodes.snow.SlushNode;

import java.util.ArrayList;
import java.util.List;

import static jabs.network.node.nodes.snow.SlushNode.SNOW_GENESIS_BLOCK;

/**
 * File: Snow.java
 * Description: Implements SnowBall protocol for JABS blockchain simulator.
 * Author: Siamak Abdi
 * Date: August 22, 2023
 */

public class SlushLANScenario extends AbstractScenario {
  protected int numNodes;
  protected double simulationStopTime;

  public SlushLANScenario(String name, long seed, int numNodes, double simulationStopTime) {
    super(name, seed);
    this.numNodes = numNodes;
    this.simulationStopTime = simulationStopTime;
  }

  @Override
  public void createNetwork() {
    network = new SlushLocalLANNetwork(randomnessEngine);
    network.populateNetwork(this.simulator, this.numNodes, new SnowConsensusConfig());
  }

  @Override
  protected void insertInitialEvents() {
    List<Node> nodes = network.getAllNodes();
    Node startNode = nodes.get(0); // the first node that starts proposing a block
    SlushNode slushNode = (SlushNode) startNode;
    List<Node> sampledNeighbors = sampleNeighbors(startNode, Snow.k);
    SnowBlock snowBlock = BlockFactory.sampleSlushBlock(simulator, network.getRandom(),
        (SlushNode) startNode, SNOW_GENESIS_BLOCK);
    slushNode.currentBlock = snowBlock;
    for (Node destination : sampledNeighbors) {
      startNode.query(
          new QueryMessage(
              new SnowPrepareQuery<>(startNode, snowBlock)),
          destination);
    }
    // -----------------------------------
    for (int n = 1; n < nodes.size(); n++) {
      List<Node> samples = sampleNeighbors(nodes.get(n), Snow.k);
      SlushNode otherNodes = (SlushNode) nodes.get(n);
      otherNodes.currentBlock = SNOW_GENESIS_BLOCK;
      for (Node destination : samples) {
        nodes.get(n).query(
            new QueryMessage(
                new SnowPrepareQuery<>(nodes.get(n), SNOW_GENESIS_BLOCK)),
            destination);
      }
    }
  }

  private List<Node> sampleNeighbors(Node node, int k) {
    List<Node> neighbors = new ArrayList<>();
    neighbors.addAll(node.getP2pConnections().getNeighbors());
    neighbors.remove(node);
    List<Node> sampledNodes = new ArrayList<>();
    for (int i = 0; i < k; i++) {
      int randomIndex = randomnessEngine.sampleInt(neighbors.size());
      sampledNodes.add(neighbors.get(randomIndex));
      neighbors.remove(randomIndex);
    }
    return sampledNodes;
  }

  @Override
  public boolean simulationStopCondition() {
    return (simulator.getSimulationTime() > this.simulationStopTime);
  }

}
