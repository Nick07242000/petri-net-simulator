package org.nnf.pns.model;

import java.util.concurrent.Semaphore;

public class Monitor {
    private static Monitor instance;
    public PetriNet petriNet;
    public Semaphore mutex;
    public Semaphore[] queueTransitions;
    public Policy policy;


    private Monitor(){

    }

    public Monitor getInstance(){
        if (instance == null) {
            instance = new Monitor();
        }
        return instance;
    }

    public void fireTransition(int transition, boolean isTaken){
        if(!isTaken) {
            try {
                mutex.acquire();
            } catch (Exception e) {
                System.out.println("caught exception");
            }
        }
        if(petriNet.fire(transition)){
            int[] transitionSensitized=petriNet.sensitized(transition);
            int[] whichWaiting = waiting(transition);
            int[] transitionWaitingSensitized = comparingVectors(transitionSensitized, whichWaiting);
            if(transitionWaitingSensitized.length>0){
                int nextTransition = policy.whichChoose(transitionSensitized); //Compara con las IT
                mutex.release();
            }
        }else{
            mutex.release();
            toWait(transition);
        }

        }



    public int[] comparingVectors(int[] sensitized, int[] waiting){
        return null;
    }

    public int[] waiting(int transition){
        //en que transiciones hay hilos esperando
        return null;
    }
    public void toWait(int transition){
        //agrega un hilo esperando por la transicion en la cola
    }
}
