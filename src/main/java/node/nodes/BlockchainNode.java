package main.java.node.nodes;

import main.java.data.*;
import main.java.message.*;
import main.java.blockchain.LocalBlockTree;
import main.java.consensus.AbstractBlockchainConsensus;
import main.java.p2p.AbstractP2PConnections;

import java.util.HashMap;
import java.util.HashSet;

public abstract class BlockchainNode<B extends Block<B>, T extends Tx<T>> extends Node {
    protected final AbstractBlockchainConsensus<B, T> consensusAlgorithm;

    protected final HashMap<TxHash<T>, T> alreadySeenTxs = new HashMap<>();
    protected final HashMap<BlockHash<B>, B> alreadySeenBlocks = new HashMap<>();
    protected final LocalBlockTree<B> localBlockTree;

    public BlockchainNode(int nodeID, int region, AbstractP2PConnections routingTable,
                          AbstractBlockchainConsensus<B, T> consensusAlgorithm) {
        super(nodeID, region, routingTable);
        this.consensusAlgorithm = consensusAlgorithm;
        this.localBlockTree = consensusAlgorithm.getLocalBlockTree();
        this.consensusAlgorithm.setNode(this);
    }

    @Override
    public void processIncomingMessage(Packet packet) {
        Message message = packet.getMessage();
        Message.MessageType messageType = message.getMessageType();
        if (messageType == Message.MessageType.DATA) {
            Data data = ((DataMessage) message).getData();
            if (data instanceof Block) {
                B block = (B) data;
                if (!localBlockTree.contains(block)){
                    localBlockTree.add(block);
                    alreadySeenBlocks.put(block.getHash(), block);
                    if (localBlockTree.getLocalBlock(block).isConnectedToGenesis) {
                        this.processNewBlock(block);
                        HashSet<B> newBlocks = localBlockTree.getAllSuccessors(block);
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
        } else if (messageType == Message.MessageType.INV) {
            Hash hash = ((InvMessage) message).getHash();
            if (hash instanceof TxHash){
                TxHash<T> txHash = (TxHash<T>) hash;
                if (!alreadySeenTxs.containsKey(hash)) {
                    alreadySeenTxs.put(txHash, null);
                    this.nodeNetworkInterface.addToUpLinkQueue(
                            new Packet(this, packet.getFrom(),
                                    new RequestDataMessage(txHash)
                            )
                    );
                }
            } else if (hash instanceof BlockHash) {
                BlockHash<B> blockHash = (BlockHash<B>) hash;
                if (!alreadySeenBlocks.containsKey(blockHash)) {
                    alreadySeenBlocks.put(blockHash, null);
                    this.nodeNetworkInterface.addToUpLinkQueue(
                            new Packet(this, packet.getFrom(),
                                    new RequestDataMessage( blockHash)
                            )
                    );
                }
            }
        } else if (messageType == Message.MessageType.REQUEST_DATA) {
            Hash hash = ((RequestDataMessage) message).getHash();
            if (hash instanceof BlockHash) {
                BlockHash<B> blockHash = (BlockHash<B>) hash;
                if (alreadySeenBlocks.containsKey(blockHash)) {
                    B block = alreadySeenBlocks.get(blockHash);
                    if (block != null) {
                        this.nodeNetworkInterface.addToUpLinkQueue(
                                new Packet(this, packet.getFrom(),
                                        new DataMessage<>(block)
                                )
                        );
                    }
                }
            } else if (hash instanceof TxHash) {
                TxHash<T> txHash = (TxHash<T>) hash;
                if (alreadySeenTxs.containsKey(txHash)) {
                    T tx = alreadySeenTxs.get(txHash);
                    if (tx != null) {
                        this.nodeNetworkInterface.addToUpLinkQueue(
                                new Packet(this, packet.getFrom(),
                                        new DataMessage<>(tx)
                                )
                        );
                    }
                }
            }
        } else if (messageType == Message.MessageType.VOTE) {
            Vote vote = ((VoteMessage) message).getVote();
            this.processNewVote(vote);
        }
    }

    protected abstract void processNewTx(T tx, Node from);
    protected abstract void processNewBlock(B block);
    protected abstract void processNewVote(Vote vote);

    public AbstractBlockchainConsensus<B, T> getConsensusAlgorithm() {
        return this.consensusAlgorithm;
    }
}
