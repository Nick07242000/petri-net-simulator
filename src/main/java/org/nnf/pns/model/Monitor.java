package org.nnf.pns.model;

import org.nnf.pns.util.Constants;

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

    public void fireTransition(int transition, boolean isTaken) {
        if (!isTaken) {
            try {
                mutex.acquire();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean sensitized = petriNet.sensitized(transition);//si esta sensibilizada
        boolean someoneWaiting = petriNet.someoneWaiting(transition);
        if (!sensitized || someoneWaiting) {
            mutex.release();
            toWait(transition);
            fireTransition(transition, true);
        }
        int[] fireSequence = getFireSequence(transition);
        petriNet.fire(fireSequence);

        int[] newSensitized = petriNet.sensitizedTransition();
        int nextTransition = policy.whichChoose(newSensitized);
        if(nextTransition!=-1){
            queueTransitions[nextTransition].release();
            return;
        }
        mutex.release();
        return;
    }


    public void toWait(int transition){
        try {
            queueTransitions[transition].acquire();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int[] getFireSequence(int transition){
        int[] fireSequence = new int[Constants.TRANS_COUNT];
        fireSequence[transition]=1;
        return fireSequence;
    }
}
