package jabs.log;

import de.siegmar.fastcsv.writer.CsvWriter;

import java.io.File;
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
    public void logBeforeEvent() {
        if (this.csvOutputConditionBeforeEvent()) {
            loggerCSV.writeRow(this.csvLineOutput());
        }
    }

    @Override
    public void logAfterEvent() {
        if (this.csvOutputConditionAfterEvent()) {
            loggerCSV.writeRow(this.csvLineOutput());
        }
    }

    @Override
    public void finalLog() throws IOException {
        if (this.csvOutputConditionFinal()) {
            loggerCSV.writeRow(this.csvLineOutput());
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
    abstract protected boolean csvOutputConditionBeforeEvent();

    /**
     * If return value is true, then one line will be written to the csv output.
     * this run after event execution.
     * @return true if one line should be written to the output CSV file.
     */
    abstract protected boolean csvOutputConditionAfterEvent();

    /**
     * If return value is true, then one line will be written to the csv output.
     * this run after event execution.
     * @return true if one line should be written to the output CSV file.
     */
    abstract protected boolean csvOutputConditionFinal();

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
