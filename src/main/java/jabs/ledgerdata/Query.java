package jabs.ledgerdata;

import jabs.network.node.nodes.Node;

public class Query extends BasicData {
    private final Node inquirer;

    protected Query(int size, Node inquirer) {
        super(size);
        this.inquirer = inquirer;
    }

    public Node getInquirer() {
        return inquirer;
    }

}
