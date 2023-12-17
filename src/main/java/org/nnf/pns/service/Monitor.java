package org.nnf.pns.service;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.model.policy.Policy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.lang.String.join;
import static java.util.Arrays.asList;
import static java.util.Arrays.fill;
import static java.util.stream.IntStream.range;
import static org.nnf.pns.util.Concurrency.tryAcquire;
import static org.nnf.pns.util.Constants.*;
import static org.nnf.pns.util.Regex.callRegexAnalyzer;

public class Monitor {
    private static final Logger log = Logger.getLogger(Monitor.class);

    private static Monitor instance;
    private final PetriNet petriNet;
    private final Policy policy;
    private final Semaphore mutex;
    private final Semaphore[] queues;
    private final int[] waiting;
    private final int[] timesFired;
    private final List<String> firedTransitions;

    private Monitor(Policy policy) {
        this.petriNet = new PetriNet(
                new Array2DRowRealMatrix(INCIDENCE_MATRIX),
                new Array2DRowRealMatrix(INITIAL_MARKING)
        );

        this.policy = policy;

        this.mutex = new Semaphore(1);

        this.queues = range(0, TRANSITIONS_COUNT)
                .mapToObj(i -> new Semaphore(0))
                .toArray(Semaphore[]::new);

        this.waiting = new int[TRANSITIONS_COUNT];
        fill(this.waiting, 0);

        this.timesFired = new int[TRANSITIONS_COUNT];

        this.firedTransitions = new ArrayList<>();
    }

    public static Monitor getInstance(Policy policy) {
        if (instance == null) instance = new Monitor(policy);
        return instance;
    }

    public void fireTransition(int transition) {
        tryAcquire(mutex);

        //Check if transition can be fired
        while (!canBeFired(transition)) {
            moveToWaiting(transition);
            tryAcquire(mutex);
        }

        //Fire the transition, evolve current marking
        petriNet.fire(transition);
        timesFired[transition]++;
        firedTransitions.add("T" + transition);
        log.debug("Transition " + transition + " fired successfully");

        //Check for program finish
        checkProgramEnd();

        //After executing transitions, check for newly sensitized ones
        wakeUpUnlockedThreads();

        mutex.release();
        log.debug("Mutex freed, remaining permits: " + mutex.availablePermits());
    }

    private boolean canBeFired(int transition) {
        if (!petriNet.isSensitized(transition))
            return false;

        if (this.waiting[transition] > 0)
            return false;

        if (asList(CONFLICTS).contains(transition)) {
            int conflict = transition + (transition % 2 == 0 ? -1 : 1);

            if (petriNet.getSensitizedTransitionNumbers().contains(conflict))
                return policy.choose(asList(transition, conflict)) == transition;
        }

        return true;
    }

    private void moveToWaiting(int transition) {
        log.debug("Thread moved to waiting list for transition: " + transition);

        //Increase waiting count
        waiting[transition]++;

        //Release the monitor
        mutex.release();

        //Sleep thread
        tryAcquire(queues[transition]);
    }


    private void wakeUpUnlockedThreads() {
        //Check for waiting threads in the newly sensitized transitions
        petriNet.getSensitizedTransitionNumbers()
                .stream()
                .filter(t -> this.waiting[t] != 0)
                .forEachOrdered(t -> {
                    this.waiting[t]--;
                    log.debug("Waking up thread for transition: " + t);
                    queues[t].release();
                });
    }

    private void checkProgramEnd() {
        if (timesFired[TRANSITIONS_COUNT - 1] == MAX_GENERATED && petriNet.hasInitialState()) {
            log.debug("PROGRAM FINISHED");
            log.info(join("", firedTransitions));
            callRegexAnalyzer();
            System.exit(0);
        }
    }
}
