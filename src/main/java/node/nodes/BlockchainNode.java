package main.java.node.nodes;

import main.java.data.*;
import main.java.message.*;
import main.java.blockchain.LocalBlockTree;
import main.java.consensus.AbstractBlockchainConsensus;
import main.java.p2p.AbstractP2PConnections;

import java.util.HashMap;
import java.util.HashSet;

public abstract class BlockchainNode<T extends Transaction<T>, B extends Block<B>> extends Node {
    protected final AbstractBlockchainConsensus<B, T> consensusAlgorithm;

    protected final HashMap<TxHash<T>, T> alreadySeenTxs = new HashMap<>();
    protected final HashMap<BlockHash<B>, B> alreadySeenBlocks = new HashMap<>();
    protected final LocalBlockTree<B> localBlockTree;

    public BlockchainNode(int nodeID, int region, AbstractP2PConnections routingTable,
                          AbstractBlockchainConsensus<B, T> consensusAlgorithm) {
        super(nodeID, region, routingTable);
        this.consensusAlgorithm = consensusAlgorithm;
        this.localBlockTree = consensusAlgorithm.getLocalBlockTree();
    }

    @Override
    public void processIncomingMessage(Message message) {
        if (message instanceof TxMessage) {
            T tx = ((TxMessage<T>) message).getTransaction();
            if (!alreadySeenTxs.containsValue(tx)){
                alreadySeenTxs.put(tx.getHash(), tx);
                this.processNewTx(tx, message.getFrom());
            }
        } else if (message instanceof BlockMessage) {
            B block = (B) ((BlockMessage<B>) message).getBlock();
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
                            new RequestBlockMessage<>(this, message.getFrom(), block.getParent().getHash())
                    );
                }
            }
        } else if (message instanceof TxInvMessage) {
            TxHash<T> txHash = ((TxInvMessage<T>) message).getTxHash();
            if (!alreadySeenTxs.containsKey(txHash)) {
                alreadySeenTxs.put(txHash, null);
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new RequestTxMessage<>(this, message.getFrom(), txHash)
                );
            }
        } else if (message instanceof BlockInvMessage) {
            BlockHash<B> blockHash = ((BlockInvMessage<B>) message).getBlockHash();
            if (!alreadySeenBlocks.containsKey(blockHash)) {
                alreadySeenBlocks.put(blockHash, null);
                this.nodeNetworkInterface.addToUpLinkQueue(
                        new RequestBlockMessage<>(this, message.getFrom(), blockHash)
                );
            }
        } else if (message instanceof RequestTxMessage) {
            TxHash<T> txHash = ((RequestTxMessage<T>) message).getTxHash();
            if (alreadySeenTxs.containsKey(txHash)) {
                T tx = alreadySeenTxs.get(txHash);
                if (tx != null) {
                    this.nodeNetworkInterface.addToUpLinkQueue(
                            new TxMessage<>(tx.getSize(), this, message.getFrom(), tx)
                    );
                }
            }
        } else if (message instanceof RequestBlockMessage) {
            BlockHash<B> blockHash = ((RequestBlockMessage<B>) message).getBlockHash();
            if (alreadySeenBlocks.containsKey(blockHash)) {
                B block = alreadySeenBlocks.get(blockHash);
                if (block != null) {
                    this.nodeNetworkInterface.addToUpLinkQueue(
                            new BlockMessage<>(block.getSize(), this, message.getFrom(), block)
                    );
                }
            }
        }
    }

    protected abstract void processNewTx(T tx, Node from);
    protected abstract void processNewBlock(B block);

    public AbstractBlockchainConsensus<B, T> getConsensusAlgorithm() {
        return this.consensusAlgorithm;
    }
}
