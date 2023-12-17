package org.nnf.pns.service;

import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

import static org.nnf.pns.util.Constants.LIMIT_FIRING;

@AllArgsConstructor
public class Generator implements Runnable{
    private static final Logger log = Logger.getLogger(Generator.class);

    private final Monitor monitor;

    @Override
    public void run() {
        for (int i = 0; i < LIMIT_FIRING; i++) {
            log.debug("Generating mark...");
            try {
                monitor.fireTransition(0, false);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            //delay(1000);
        }
    }
}