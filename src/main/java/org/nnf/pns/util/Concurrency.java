package org.nnf.pns.util;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.nnf.pns.service.Generator;
import org.nnf.pns.service.Monitor;
import org.nnf.pns.service.Worker;

import java.util.concurrent.Semaphore;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static lombok.AccessLevel.PRIVATE;
import static org.nnf.pns.util.Net.castToInt;
import static org.nnf.pns.util.Net.createSequence;

@NoArgsConstructor(access = PRIVATE)
public final class Concurrency {
    private static final Logger log = Logger.getLogger(Concurrency.class);

    public static void tryAcquire(Semaphore s) {
        try {
            s.acquire();
        } catch (InterruptedException e) {
            log.debug("Error acquiring semaphore:", e);
            currentThread().interrupt();
        }
    }

    public static void delay(int ms) {
        try {
            sleep(ms);
        } catch (InterruptedException e){
            log.debug("Error on thread delay:", e);
            currentThread().interrupt();
        }
    }

    public static void createGenerator(Monitor monitor) {
        Thread thread = new Thread(
                new Generator(monitor),
                "BLUE");

        thread.start();
        log.debug("Generator started...");
    }

    public static void createWorker(String name, Monitor monitor, int... transitions) {
        Thread thread = new Thread(
                new Worker(monitor, castToInt(createSequence(transitions))),
                name.toUpperCase()
        );

        log.debug("Worker " + name.toLowerCase() + " started...");
        thread.start();
    }
}
