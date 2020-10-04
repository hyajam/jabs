package main.java.node.nodes;

import main.java.blockchain.LocalBlockTree;
import main.java.consensus.AbstractBlockchainConsensus;
import main.java.data.*;
import main.java.message.*;
import main.java.p2p.AbstractP2PConnections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedSet;

public abstract class BlockchainNode<B extends Block<B>, T extends Tx<T>> extends Node {
    protected final AbstractBlockchainConsensus<B, T> consensusAlgorithm;

    protected final HashMap<Hash, T> alreadySeenTxs = new HashMap<>();
    protected final HashMap<Hash, B> alreadySeenBlocks = new HashMap<>();
    protected final HashSet<Vote> alreadySeenVotes = new HashSet<>();
    protected final LocalBlockTree<B> localBlockTree;

    public BlockchainNode(int nodeID, long downloadBandwidth, long uploadBandwidth, AbstractP2PConnections routingTable,
                          AbstractBlockchainConsensus<B, T> consensusAlgorithm) {
        super(nodeID, downloadBandwidth, uploadBandwidth, routingTable);
        this.consensusAlgorithm = consensusAlgorithm;
        this.localBlockTree = consensusAlgorithm.getLocalBlockTree();
        this.consensusAlgorithm.setNode(this);
    }

    @Override
    public void processIncomingPacket(Packet packet) {
        Message message = packet.getMessage();
        if (message instanceof DataMessage) {
            Data data = ((DataMessage) message).getData();
            if (data instanceof Block) {
                B block = (B) data;
                if (!localBlockTree.contains(block)){
                    localBlockTree.add(block);
                    alreadySeenBlocks.put(block.getHash(), block);
                    if (localBlockTree.getLocalBlock(block).isConnectedToGenesis) {
                        this.processNewBlock(block);
                        SortedSet<B> newBlocks = localBlockTree.getAllSuccessors(block);
                        for (B newBlock:newBlocks){
                            this.processNewBlock(newBlock);
                        }
                    } else {
                        this.nodeNetworkInterface.addToUpLinkQueue(
                            new Packet(this, packet.getFrom(),
                                    new RequestDataMessage(block.getParent().getHash())
                            )
                        );
                    }
                }
            } else if (data instanceof Tx) {
                T tx = (T) data;
                if (!alreadySeenTxs.containsValue(tx)){
                    alreadySeenTxs.put(tx.getHash(), tx);
                    this.processNewTx(tx, packet.getFrom());
                }
            }
        } else if (message instanceof InvMessage) {
            Hash hash = ((InvMessage) message).getHash();
            if (hash.getData() instanceof Block){
                if (!alreadySeenTxs.containsKey(hash)) {
                    alreadySeenTxs.put(hash, null);
                    this.nodeNetworkInterface.addToUpLinkQueue(
                            new Packet(this, packet.getFrom(),
                                    new RequestDataMessage(hash)
                            )
                    );
                }
            } else if (hash.getData() instanceof Tx) {
                if (!alreadySeenBlocks.containsKey(hash)) {
                    alreadySeenBlocks.put(hash, null);
                    this.nodeNetworkInterface.addToUpLinkQueue(
                            new Packet(this, packet.getFrom(),
                                    new RequestDataMessage(hash)
                            )
                    );
                }
            }
        } else if (message instanceof RequestDataMessage) {
            Hash hash = ((RequestDataMessage) message).getHash();
            if (hash.getData() instanceof Block) {
                if (alreadySeenBlocks.containsKey(hash)) {
                    B block = alreadySeenBlocks.get(hash);
                    if (block != null) {
                        this.nodeNetworkInterface.addToUpLinkQueue(
                                new Packet(this, packet.getFrom(),
                                        new DataMessage(block)
                                )
                        );
                    }
                }
            } else if (hash.getData() instanceof Tx) {
                if (alreadySeenTxs.containsKey(hash)) {
                    T tx = alreadySeenTxs.get(hash);
                    if (tx != null) {
                        this.nodeNetworkInterface.addToUpLinkQueue(
                                new Packet(this, packet.getFrom(),
                                        new DataMessage(tx)
                                )
                        );
                    }
                }
            }
        } else if (message instanceof VoteMessage) {
            Vote vote = ((VoteMessage) message).getVote();
            if (!alreadySeenVotes.contains(vote)) {
                alreadySeenVotes.add(vote);
                this.processNewVote(vote);
            }
        }
    }

    protected abstract void processNewTx(T tx, Node from);
    protected abstract void processNewBlock(B block);
    protected abstract void processNewVote(Vote vote);

    public AbstractBlockchainConsensus<B, T> getConsensusAlgorithm() {
        return this.consensusAlgorithm;
    }

    public int numberOfAlreadySeenBlocks() {
        return alreadySeenBlocks.size();
    }
}
