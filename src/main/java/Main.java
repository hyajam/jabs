package main.java;

import main.java.scenario.AbstractScenario;
import main.java.scenario.EthereumDAGsperNetworkScenario;

public class Main {
    public static void main(String[] args) {
        AbstractScenario scenario;
        double[] rate = {1, 0.71, 0.5, 0.2, 0.1, 0.071};

        for (double v : rate) {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(v);
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();

            for (int i = 0; i < 10; i++) {
                System.out.println();
                System.out.printf("### Simulation number: %d ###\n", i);
                scenario = new EthereumDAGsperNetworkScenario(i + 140,
                        40, 100,
                        7,
                        (long) (100 * (1 / v)), v, v);
                scenario.run();
            }
        }
    }
}
