package org.nnf.pns.service;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

import static java.lang.Thread.currentThread;
import static org.nnf.pns.util.Constants.MAX_GENERATED;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@AllArgsConstructor
public class Worker implements Runnable {
    private static final Logger log = Logger.getLogger(Worker.class);

    private final Monitor monitor;
    private final int[] transition;

    @Override
    public void run() {
        for (int i = 0; i < MAX_GENERATED; i++) {
            for (int j = 0; j < TRANSITIONS_COUNT; j++) {
                if (transition[j] == 1) {
                    log.debug("Thread " + currentThread().getName() + " firing transition " + j);
                    monitor.fireTransition(j, false);
                }
            }
        }
    }
}
