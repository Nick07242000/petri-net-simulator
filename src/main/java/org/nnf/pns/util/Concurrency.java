package org.nnf.pns.util;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class Concurrency {
    private static final Logger log = Logger.getLogger(Concurrency.class);

    public static void tryAcquire(Semaphore s) {
        try {
            s.acquire();
        } catch (InterruptedException e) {
            log.error("Error acquiring semaphore: {}", e);
            currentThread().interrupt();
        }
    }

    public static void delay(int ms) {
        try {
            sleep(ms);
        } catch (InterruptedException e){
            log.error(e.getMessage());
            currentThread().interrupt();
        }
    }
}
