package psc.services.pasoescheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasOEScheduler {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    public PasOEScheduler() {
        log.info("New PasOEScheduler created");
    }

    /**
     * Any method can be used as a Task, as long as there are no parameters
     */
    public void printMessages() {
        log.info("Hello Next2019");
    }
}
