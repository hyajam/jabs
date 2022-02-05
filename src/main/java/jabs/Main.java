package jabs;

import jabs.scenario.AbstractScenario;
import jabs.scenario.PBFTLANScenario;
import de.siegmar.fastcsv.writer.CsvWriter;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        AbstractScenario scenario;

        CsvWriter csv = CsvWriter.builder().build(Paths.get("_output/simulation-log.csv"));

        scenario = new PBFTLANScenario(1234, csv, 100, 1000000);
        scenario.run();

        csv.close();
    }
}
