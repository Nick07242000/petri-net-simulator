package org.nnf.pns.model;

import org.nnf.pns.util.Constants;

public class PetriNet {
    public int[][] incidenceMatrix;
    public int[][] backwardMatrix;
    public int[] currentMarking;
    public PetriNet(int[][] incidenceMatrix, int[][] backwardMatrix, int[] currentMarking){
    this.incidenceMatrix=incidenceMatrix;
    this.backwardMatrix=backwardMatrix;
    this.currentMarking=currentMarking;
    }

    public void fire(int[] transition){
        //actualiza la marca segun la ecuacion de estado RdP
    }
    public boolean isSensitized(int transition){
        return sensitizedTransitions()[transition]==1;

    }
    public int[] sensitizedTransitions(){
        int[] sensitizedTransitions=new int[Constants.TRANS_COUNT];
        for(int i=0; i<Constants.TRANS_COUNT; i++){
            for(int j=0; j<Constants.PLACES_COUNT; j++){
                if(backwardMatrix[j][i]>0&&currentMarking[i]<backwardMatrix[j][i]){
                    sensitizedTransitions[i]=0;
                    break;
                }else{
                    sensitizedTransitions[i]=1;
                }
            }
        }
        return sensitizedTransitions;
    }
}
