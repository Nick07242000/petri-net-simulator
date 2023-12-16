package org.nnf.pns.service;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.model.policy.Policy;

import java.util.List;
import java.util.concurrent.Semaphore;

import static java.util.Arrays.fill;
import static java.util.stream.IntStream.range;
import static org.nnf.pns.util.Concurrency.tryAcquire;
import static org.nnf.pns.util.Constants.*;

public class Monitor {
    private static final Logger log = Logger.getLogger(Monitor.class);

    private static Monitor instance;
    private final PetriNet petriNet;
    private final Policy policy;
    private final Semaphore mutex;
    private final Semaphore[] queues;
    private final int[] waiting;
    private final int[] timesFired;

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

        this.timesFired =new int[TRANSITIONS_COUNT];
    }

    public static Monitor getInstance(Policy policy) {
        if (instance == null) instance = new Monitor(policy);
        return instance;
    }

    public void fireTransition(int transition, boolean isTaken) {
        log.debug("Thread firing for transition: " + transition);

        //Only on first call should mutex be taken
        if (!isTaken) tryAcquire(mutex);

        //Check if transition can be fired
        if (!petriNet.isSensitized(transition) || this.waiting[transition] > 0) {
            moveToWaiting(transition);
            return;
        }

        //Fire the transition, evolve current marking
        petriNet.fire(transition);
        timesFired[transition]++;

        //Check for program finish
        if (finalized()) {
            log.debug("PROGRAM FINISHED");
            return;
        }

        checkNextTransition();

        if (!isTaken) {
            mutex.release();
            log.debug("Mutex freed, remaining permits: " + mutex.availablePermits());
        }
    }

    private void moveToWaiting(int transition) {
        log.debug("Thread moved to waiting list for transition: " + transition);

        //Increase waiting count and ????
        waiting[transition]++;
        tryAcquire(queues[transition]);
        fireTransition(transition, true);

        //Release the monitor
        mutex.release();
    }

    private void checkNextTransition() {
        //Get the next transition via policy
        List<Integer> sensitized = petriNet.getSensitizedTransitionNumbers();
        int nextTransition = policy.choose(sensitized);
        log.debug("Chosen transition: " + nextTransition);

        if (this.waiting[nextTransition] == 0)
            return;

        //Attempt to wake up thread
        this.waiting[nextTransition]--;
        log.debug("Waking up thread for transition: " + nextTransition);
        queues[nextTransition].release();
    }

    private boolean finalized() {
        return timesFired[TRANSITIONS_COUNT - 1] == LIMIT_FIRING && petriNet.isFinished();
    }
}
