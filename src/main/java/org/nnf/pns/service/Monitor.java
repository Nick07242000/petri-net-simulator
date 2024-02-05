package org.nnf.pns.service;


import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.model.policy.Policy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

import static java.lang.String.join;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.exit;
import static java.util.Arrays.fill;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.nnf.pns.util.Concurrency.delay;
import static org.nnf.pns.util.Concurrency.tryAcquire;
import static org.nnf.pns.util.Constants.MAX_GENERATED;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;
import static org.nnf.pns.util.Regex.callRegexAnalyzer;

public class Monitor {
    private static final Logger log = Logger.getLogger(Monitor.class);

    private static Monitor instance;
    private final PetriNet petriNet;
    private final Policy policy;
    private final Semaphore mutex;
    private final Semaphore[] queues;
    private final boolean[] waiting;
    private final int[] timesFired;
    private final List<String> firedTransitions;

    private Monitor(Policy policy) {
        this.petriNet = new PetriNet();

        this.policy = policy;

        this.mutex = new Semaphore(1);

        this.queues = range(0, TRANSITIONS_COUNT)
                .mapToObj(i -> new Semaphore(0))
                .toArray(Semaphore[]::new);

        this.waiting = new boolean[TRANSITIONS_COUNT];
        fill(this.waiting, false);

        this.timesFired = new int[TRANSITIONS_COUNT];

        this.firedTransitions = new ArrayList<>();
    }

    public static Monitor getInstance(Policy policy) {
        if (instance == null) instance = new Monitor(policy);
        return instance;
    }

    public void fireTransition(int transition, boolean isTaken) {
        //Only on first call should mutex be taken
        if (!isTaken) tryAcquire(mutex);

        //Check if transition can be fired
        if (!petriNet.isSensitized(transition)) {
            moveToWaiting(transition);
            return;
        }

        //Check if transition is timed and within the time window (t > alpha)
        if (petriNet.isTimed(transition) && !petriNet.isInWindow(transition)) {
            mutex.release();
            delay(petriNet.getTimeDelay(transition));
            fireTransition(transition, false);
            return;
        }

        //Fire the transition, evolve current marking
        petriNet.fire(transition);
        timesFired[transition]++;
        firedTransitions.add("T" + transition);
        log.debug("Transition " + transition + " fired successfully");

        //Check for program finish
        checkProgramEnd();

        //After executing transitions, check for newly sensitized ones
        if (wakeUpNextTransition()) return;

        mutex.release();
        log.debug("Mutex freed, remaining permits: " + mutex.availablePermits());
    }

    private void moveToWaiting(int transition) {
        log.debug("Thread moved to waiting list for transition: " + transition);

        //Increase waiting count
        waiting[transition] = true;

        //Release the monitor
        mutex.release();

        //Sleep thread
        tryAcquire(queues[transition]);

        //Resume on wake up
        //tryAcquire(mutex);
        fireTransition(transition, true);
    }

    private boolean wakeUpNextTransition() {
        List<Integer> waitingTransitions = petriNet.getSensitizedTransitionNumbers()
                .stream()
                .filter(t -> this.waiting[t])
                .collect(toList());

        //If no transitions are waiting there's nothing to wake up
        if (waitingTransitions.isEmpty()) return false;

        //Ask the policy for the next transition to be fired
        int next = policy.choose(waitingTransitions);

        this.waiting[next] = false;
        log.debug("Waking up thread for transition: " + next);
        queues[next].release();
        return true;
    }

    private void checkProgramEnd() {
        if (timesFired[TRANSITIONS_COUNT - 1] == MAX_GENERATED && petriNet.hasInitialState()) {
            log.debug("PROGRAM FINISHED");
            log.info(join("", firedTransitions));
            callRegexAnalyzer();
            log.debug("End time: "+currentTimeMillis());
            exit(0);
        }
    }
}
