package jabs.consensus.algorithm;

import jabs.ledgerdata.Block;
import jabs.ledgerdata.Tx;
import jabs.ledgerdata.Query;

public interface QueryingBasedConsensus <B extends Block<B>, T extends Tx<T>> extends ConsensusAlgorithm<B, T>{
    void newIncomingQuery(Query query);
}
