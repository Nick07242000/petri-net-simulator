package org.nnf.pns.service;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

import static org.nnf.pns.util.Concurrency.delay;
import static org.nnf.pns.util.Constants.MAX_GENERATED;
import static org.nnf.pns.util.Constants.TIMED_TRANSITIONS;

@AllArgsConstructor
public class Generator implements Runnable{
    private static final Logger log = Logger.getLogger(Generator.class);

    private final Monitor monitor;

    @Override
    public void run() {
        for (int i = 0; i < MAX_GENERATED; i++) {
            log.debug("Generating mark...");
            monitor.fireTransition(0);
            delay(TIMED_TRANSITIONS.get(0));
        }
    }
}
