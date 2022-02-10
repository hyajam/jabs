package jabs.log;

import jabs.scenario.AbstractScenario;

import java.io.IOException;

public abstract class AbstractLogger {
    protected AbstractScenario scenario;

    public void setScenario(AbstractScenario scenario) {
        this.scenario = scenario;
    }

    abstract public void initialLog();
    abstract public void logBeforeEvent();
    abstract public void logAfterEvent();
    abstract public void finalLog() throws IOException;
}
