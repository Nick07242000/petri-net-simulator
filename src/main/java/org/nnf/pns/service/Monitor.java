package org.nnf.pns.service;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
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
                new Array2DRowRealMatrix(INCIDENCE_MATRIX),
                new Array2DRowRealMatrix(INITIAL_MARKING)
        );

        this.policy = policy;

        this.mutex = new Semaphore(1);

        this.queue = generate(() -> this.mutex).limit(TRANSITIONS_COUNT).toArray(Semaphore[]::new);

        this.waiting = new int[TRANSITIONS_COUNT];
        fill(this.waiting, 0);
    }

    public static Monitor getInstance(Policy policy) {
        if (instance == null) instance = new Monitor(policy);
        return instance;
    }

    public void fireTransition(int transition, boolean isTaken) {
       takeMutex(isTaken);

        //If petri net is not sensitized OR some thread is waiting for the transition to be sensitized
        if (!petriNet.isSensitized(transition) || this.waiting[transition] > 0) {
            mutex.release();
            increaseWaitingThreads(transition);
            fireTransition(transition, true);
        }

        petriNet.fire(transition); //cambiar la marca actual de la red de petri

        int[] newSensitized = petriNet.getSensitizedTransitions(); //obtener las transiciones sensibilizadas luego del disparo
        int nextTransition = policy.choose(newSensitized); //elegir una para disparar

        if (this.waiting[nextTransition] > 0) {
            queue[nextTransition].release(); //despierta al hilo que espera por la nueva transicion
            return;
        }

        mutex.release();
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
