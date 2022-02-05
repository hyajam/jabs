package jabs.scenario;

import de.siegmar.fastcsv.writer.CsvWriter;
import jabs.network.Network;
import jabs.random.Random;
import jabs.simulator.Simulator;

public abstract class AbstractScenario {
    protected Network network;
    public Simulator simulator;
    protected Random random;
    protected CsvWriter outCSV;

    /**
     * Create the network and set up the simulation environment.
     */
    abstract protected void createNetwork();

    /**
     * Insert initial events into the event queue.
     */
    abstract protected void insertInitialEvents();

    /**
     * runs before each event and checks if simulation should stop.
     * @return true if simulation should not continue to execution of next event.
     */
    abstract protected boolean simulationStopCondition();

    /**
     * It executes when simulation is ended. It could be used to output final results.
     */
    abstract protected void finishSimulation();

    /**
     * If return value is true, then one line will be written to the csv output.
     * this run before event execution.
     * @return true if one line should be written to the output CSV file.
     */
    abstract protected boolean csvOutputConditionBeforeEvent();

    /**
     * If return value is true, then one line will be written to the csv output.
     * this run after event execution.
     * @return true if one line should be written to the output CSV file.
     */
    abstract protected boolean csvOutputConditionAfterEvent();

    /**
     * First line of CSV file which has the header information
     * @return list of header names to output
     */
    abstract protected String[] csvHeaderOutput();

    /**
     * If the CSV Output is true the returned value of this function will be written to csv output file
     * @return list of strings to output
     */
    abstract protected String[] csvLineOutput();

    /**
     * creates an abstract scenario
     * @param seed this value gives the simulation a random seed
     * @param outCSV this is output file of the scenario
     */
    public AbstractScenario(long seed, CsvWriter outCSV) {
        this.random = new Random(seed);
        simulator = new Simulator();
        this.outCSV = outCSV;
    }

    // TODO: add timed output that shows simulation is running...

    /**
     * When called starts the simulation and runs everything to the end of simulation
     */
    public void run() {
        this.createNetwork();
        this.insertInitialEvents();

        outCSV.writeRow(this.csvHeaderOutput());
        while (simulator.thereIsMoreEvents() && !this.simulationStopCondition()) {
            if (this.csvOutputConditionBeforeEvent()) {
                outCSV.writeRow(this.csvLineOutput());
            }
            simulator.executeNextEvent();
            if (this.csvOutputConditionAfterEvent()) {
                outCSV.writeRow(this.csvLineOutput());
            }
        }

        this.finishSimulation();
    }
}
