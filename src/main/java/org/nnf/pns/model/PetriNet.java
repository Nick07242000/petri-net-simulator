package org.nnf.pns.model;

import org.nnf.pns.util.Constants;

public class PetriNet {
    public int[][] incidenceMatrix;
    public int[][] backwardMatrix;
    public int[] currentMarking;
    public PetriNet(){

    }

    public void fire(int[] transition){
        //actualiza la marca segun la ecuacion de estado RdP
    }
    public boolean sensitized(int transition){

        return false;
    }
    public boolean someoneWaiting(int transition){
        //en que transiciones hay hilos esperando
        return false;
    }
    public int[] sensitizedTransition(){
        return null;
    }
}
