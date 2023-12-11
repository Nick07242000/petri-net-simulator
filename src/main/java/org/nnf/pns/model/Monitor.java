package org.nnf.pns.model;

import org.nnf.pns.util.Constants;

import java.util.concurrent.Semaphore;

public class Monitor {
    private static Monitor instance;
    public PetriNet petriNet;
    public Semaphore mutex;
    public Semaphore[] queueTransitions;
    public int[] someoneWaiting;
    public Policy policy;


    private Monitor(){
        petriNet= new PetriNet(Constants.INCIDENCE_MATRIX, Constants.BACKWARD_MATRIX, Constants.INITIAL_MARKING);
        mutex=new Semaphore(1);
        queueTransitions=new Semaphore[Constants.TRANS_COUNT];
        someoneWaiting=new int[Constants.TRANS_COUNT];
        for(int i=0; i<Constants.TRANS_COUNT; i++){
            queueTransitions[i]= new Semaphore(0);
            someoneWaiting[i]=0;
        }

        policy=Policy.getInstance();
    }

    public static Monitor getInstance(){
        if (instance == null) {
            instance = new Monitor();
        }
        return instance;
    }

    public void fireTransition(int transition, boolean isTaken) {
        if (!isTaken) {
            try {
                mutex.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean sensitized = petriNet.isSensitized(transition);//si esta sensibilizada la transicion
        boolean waiting = someoneWaiting[transition]>0; //si hay algun hilo esperando a que se sensibilice la transicion
        if (!sensitized || waiting) {
            mutex.release();
            toWait(transition);
            fireTransition(transition, true);
        }
        int[] fireSequence = getFireSequence(transition); //obtener la secuencia de disparo de la transicion
        petriNet.fire(fireSequence); //cambiar la marca actual de la red de petri

        int[] newSensitized = petriNet.sensitizedTransitions(); //obtener las transiciones sensibilizadas luego del disparo
        int nextTransition = policy.whichChoose(newSensitized); //elegir una para disparar
        if(someoneWaiting[nextTransition]>0){
            queueTransitions[nextTransition].release(); //despierta al hilo que espera por la nueva transicion
            return;
        }
        mutex.release();
    }


    public void toWait(int transition){
        try {
            queueTransitions[transition].acquire();
            someoneWaiting[transition]++;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int[] getFireSequence(int transition){
        int[] fireSequence = new int[Constants.TRANS_COUNT];
        for(int i=0;i<Constants.TRANS_COUNT; i++){
            if(i==transition){
                fireSequence[i]=1;
            }else{
                fireSequence[i]=0;
            }
        }
        return fireSequence;
    }

}
