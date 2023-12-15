package org.nnf.pns.service;


import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.log4j.Logger;
import org.nnf.pns.model.PetriNet;
import org.nnf.pns.model.policy.BalancedPolicy;
import org.nnf.pns.model.policy.Policy;
import org.nnf.pns.util.Constants;

import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

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
    private final int[] firingCount;
    private static final Logger log = Logger.getLogger(PetriNet.class);


    private Monitor(Policy policy) {
        this.petriNet = new PetriNet(
                new Array2DRowRealMatrix(INCIDENCE_MATRIX),
                new Array2DRowRealMatrix(INITIAL_MARKING)
        );

        this.policy = policy;

        this.mutex = new Semaphore(1);

        this.queue = IntStream.range(0, TRANSITIONS_COUNT)
                .mapToObj(i -> new Semaphore(0))
                .toArray(Semaphore[]::new);

        this.waiting = new int[TRANSITIONS_COUNT];
        fill(this.waiting, 0);

        this.firingCount=new int[TRANSITIONS_COUNT];
    }

    public static Monitor getInstance(Policy policy) {
        if (instance == null) instance = new Monitor(policy);
        return instance;
    }

    public void fireTransition(int transition, boolean isTaken) {
       takeMutex(isTaken);
        log.debug("Entrando hilo de la transicion: " + transition);

        //If petri net is not sensitized OR some thread is waiting for the transition to be sensitized
        if (!petriNet.isSensitized(transition) || this.waiting[transition] > 0) {
            mutex.release();
            log.debug("Hilo esperando por la transicion: " + transition);
            increaseWaitingThreads(transition);
            fireTransition(transition, true);
            return;
        }

        petriNet.fire(transition); //cambiar la marca actual de la red de petri
        firingCount[transition]++;
        if(finalized()){
            System.out.println("programa terminado");
            return;
        }
        int[] newSensitized = petriNet.getSensitizedTransitions(); //obtener las transiciones sensibilizadas luego del disparo
        int nextTransition = policy.choose(newSensitized); //elegir una para disparar
        if (this.waiting[nextTransition] > 0) {
            this.waiting[nextTransition]--;
            log.debug("Despertando hilo de la transicion: " + nextTransition);
            queue[nextTransition].release(); //despierta al hilo que espera por la nueva transicion
        } else log.debug("No hay hilos esperando por la transicion: " + nextTransition);

        if (!isTaken) {
            mutex.release();
            System.out.println("Mutex liberado, permisos: " + mutex.availablePermits());
        }
    }

    private void increaseWaitingThreads(int transition){
        try {
            waiting[transition]++;
            queue[transition].acquire();
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
    public boolean finalized(){
        return (firingCount[TRANSITIONS_COUNT-1] == Constants.LIMIT_FIRING)&&(petriNet.finished());
    }


}
