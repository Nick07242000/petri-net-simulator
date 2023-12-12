package org.nnf.pns.model;

import org.nnf.pns.util.Constants;

import java.util.ArrayList;

public class Policy {
    private static Policy instance;
    private static int leftBranchCount;
    private static int rightBranchCount;
    private Policy(){
        leftBranchCount=0;
        rightBranchCount=0;
    }

    public static Policy getInstance(){
        if (instance == null) {
            instance = new Policy();
        }
        return instance;
    }

    public synchronized int whichChoose(int[] transitions){
        int chosenTransition = 0;
        if(leftBranchCount>rightBranchCount||(leftBranchCount==0&&rightBranchCount==0)){
            chosenTransition = lookingForTransitions(transitions).stream().filter(trans -> trans % 2 == 0).findFirst().orElse(-1);
            rightBranchCount++;
        }else if(leftBranchCount<rightBranchCount) {
            chosenTransition = lookingForTransitions(transitions).stream().filter(trans -> trans % 2 != 0).findFirst().orElse(-1);
            leftBranchCount++;
        }
        return chosenTransition;
    }
    public ArrayList<Integer> lookingForTransitions(int[] transitions){
        ArrayList<Integer> indexTransitions=new ArrayList<>();
        for(int i=0; i< Constants.TRANS_COUNT; i++){
            if(transitions[i]==1){
                indexTransitions.add(i);
            }
        }
        return indexTransitions;
    }
}
