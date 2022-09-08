package jabs.log;

import de.siegmar.fastcsv.writer.CsvWriter;
import jabs.network.node.nodes.Node;
import jabs.simulator.event.Event;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;

public abstract class AbstractCSVLogger extends AbstractLogger {
    protected CsvWriter loggerCSV;

    @Override
    public void initialLog() {
        loggerCSV.writeComment(this.csvStartingComment());
        loggerCSV.writeRow(this.csvHeaderOutput());
    }

    @Override
    public void logBeforeEachEvent(Event event) {
        if (this.csvOutputConditionBeforeEvent(event)) {
            loggerCSV.writeRow(this.csvEventOutput(event));
        }
    }

    @Override
    public void logAfterEachEvent(Event event) {
        if (this.csvOutputConditionAfterEvent(event)) {
            loggerCSV.writeRow(this.csvEventOutput(event));
        }
    }

    @Override
    public void finalLog() throws IOException {
        if (this.csvOutputConditionFinalPerNode()) {
            for (Object node: this.scenario.getNetwork().getAllNodes()) {
                loggerCSV.writeRow(this.csvNodeOutput(((Node) node)));
            }
        }
        loggerCSV.close();
    }

    /**
     * Adds the Starting Comment to the output CSV file
     * @return the string to be written at the start of csv file
     */
    abstract protected String csvStartingComment();

    /**
     * If return value is true, then one line will be written to the csv output.
     * this run before event execution.
     * @return true if one line should be written to the output CSV file.
     */
    abstract protected boolean csvOutputConditionBeforeEvent(Event event);

    /**
     * If return value is true, then one line will be written to the csv output.
     * this run after event execution.
     * @return true if one line should be written to the output CSV file.
     */
    abstract protected boolean csvOutputConditionAfterEvent(Event event);

    /**
     * If true, at the end of the simulation for each node a single line output
     * would be printed which is determined by the csvNodeOutput() function
     * @return true one line per each node will be written to csv output
     */
    abstract protected boolean csvOutputConditionFinalPerNode();

    /**
     * First line of CSV file which has the header information
     * @return list of header names to output
     */
    abstract protected String[] csvHeaderOutput();

    /**
     * If the CSV Output is true the returned value of this function will
     * be written to csv output file
     * @return list of strings to output
     */
    abstract protected String[] csvEventOutput(Event event);

    /**
     * If csvOutputConditionFinalPerNode() is true the returned value of this
     * function will be written to csv output file
     * @return list of strings to output
     */
    protected String[] csvNodeOutput(Node node) {
        return new String[0];
    };

    /**
     * creates an abstract CSV logger
     * @param writer this is output CSV of the logger
     */
    public AbstractCSVLogger(Writer writer) {
        this.loggerCSV = CsvWriter.builder().build(writer);
    }

    /**
     * creates an abstract CSV logger
     * @param path this is output path of CSV file
     */
    public AbstractCSVLogger(Path path) throws IOException {
        path.toFile().getParentFile().mkdirs();
        this.loggerCSV = CsvWriter.builder().build(path);
    }
}
