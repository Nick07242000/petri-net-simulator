package org.nnf.pns.service;

import org.ejml.simple.SimpleMatrix;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.model.policy.Policy;

import java.util.concurrent.Semaphore;

import static java.util.Arrays.fill;
import static java.util.stream.Stream.generate;
import static org.nnf.pns.util.Constants.*;

public class Monitor {
    private static Monitor instance;
    private final PetriNet petriNet;
    private final Policy policy;
    private final Semaphore mutex;
    private final Semaphore[] queue;
    private final int[] waiting;

    private Monitor(Policy policy) {
        this.petriNet = new PetriNet(
                new SimpleMatrix(FORWARD_INCIDENCE_MATRIX),
                new SimpleMatrix(BACKWARDS_INCIDENCE_MATRIX),
                INITIAL_MARKING
        );

        this.policy = policy;

        this.mutex = new Semaphore(1);

        this.queue = generate(() -> this.mutex).limit(TRANSITIONS_COUNT).toArray(Semaphore[]::new);

        this.waiting = new int[TRANSITIONS_COUNT];
        fill(this.waiting, 0);
    }

    public static Monitor getInstance(Policy policy) {
        return instance == null ?
                new Monitor(policy) :
                instance;
    }

    public void fireTransition(int transition, boolean isTaken) {
        takeMutex(isTaken);
        System.out.println("take Mutex");
        //If petri net is not sensitized OR some thread is waiting for the transition to be sensitized
        if (!petriNet.isSensitized(transition) || this.waiting[transition] > 0) {
            mutex.release();
            increaseWaitingThreads(transition);
            fireTransition(transition, true);
            return;
        }

        petriNet.fire(getFireSequence(transition)); //cambiar la marca actual de la red de petri

        int[] newSensitized = petriNet.getSensitizedTransitions(); //obtener las transiciones sensibilizadas luego del disparo
        int nextTransition = policy.choose(newSensitized); //elegir una para disparar

        if (this.waiting[nextTransition] > 0) {
            queue[nextTransition].release(); //despierta al hilo que espera por la nueva transicion
            return;
        }

        mutex.release(); //TODO: problema cuando un hilo es cola de espera entra al monitor sin el mutex y ejecuta el release, aumentando el contador del mutex

    }

    public double[] getFireSequence(int transition){
        double[] fireSequence = new double[TRANSITIONS_COUNT];
        for (int i = 0; i < TRANSITIONS_COUNT; i++) {
            if (i == transition) {
                fireSequence[i] = 1;
            } else {
                fireSequence[i] = 0;
            }
        }
        return fireSequence;
    }

    private void increaseWaitingThreads(int transition){
        try {
            queue[transition].acquire();
            waiting[transition]++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void takeMutex(boolean isTaken){
        if (!isTaken) {
            try {
                mutex.acquire();
            } catch (Exception e) {
                //TODO: handle
                e.printStackTrace();
            }
        }
    }
}