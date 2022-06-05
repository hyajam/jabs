package jabs.ledgerdata.casper;

import jabs.ledgerdata.Block;

import java.util.Objects;

public class CasperFFGLink<B extends Block<B>> {
    private final B toBeJustified;
    private final B toBeFinalized;
    public static final int CASPER_VOTE_SIZE = 200;

    public CasperFFGLink(B toBeFinalized, B toBeJustified) {
        this.toBeJustified = toBeJustified;
        this.toBeFinalized = toBeFinalized;
    }

    public final B getToBeJustified() {
        return this.toBeJustified;
    }
    public final B getToBeFinalized() {
        return this.toBeFinalized;
    }

    public final String toString() { return "vote:" + toBeJustified + "->" + toBeFinalized; }

    public final int hashCode() {
        return Objects.hashCode(toBeJustified) ^ Objects.hashCode(toBeFinalized);
    }

    public final boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof CasperFFGLink) {
            CasperFFGLink<?> e = (CasperFFGLink<?>) o;
            return Objects.equals(toBeJustified, e.getToBeJustified()) &&
                    Objects.equals(toBeFinalized, e.getToBeFinalized());
        }
        return false;
    }
}