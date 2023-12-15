package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.nnf.pns.Main;
import org.nnf.pns.util.Constants;

import java.util.*;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy implements Policy {
    private static BalancedPolicy instance;
    private int leftBranchCount = 0;
    private int rightBranchCount = 0;
    public final int[] transitionsCounter=new int[TRANSITIONS_COUNT];
    private static final Logger log = Logger.getLogger(Main.class);

    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public synchronized int choose(int[] transitions) {
        int chosenTransition = 0;

        if (leftBranchCount > rightBranchCount || leftBranchCount ==  rightBranchCount ){
            if(lookingForTransitions(transitions).size()>1){
            chosenTransition= lookingForTransitions(transitions).stream().filter(t->t%2==0).findFirst().orElse(transitions[0]);
            }else{
                chosenTransition=lookingForTransitions(transitions).get(0);
            }

        } else  {
            if(lookingForTransitions(transitions).size()>1){
                chosenTransition= lookingForTransitions(transitions).stream().filter(t->t%2!=0).findFirst().orElse(transitions[0]);
            }else{
                chosenTransition=lookingForTransitions(transitions).get(0);
            }

        }
        log.debug("chosenTransition: " + chosenTransition);
        increaseFireBranch(chosenTransition);
        System.out.println("rama derecha: "+rightBranchCount);
        System.out.println("rama izquierda: "+leftBranchCount);
        return chosenTransition;
    }


    public ArrayList<Integer> lookingForTransitions(int[] transitions){
        ArrayList<Integer> indexTransitions = new ArrayList<>();

        for (int i = 1; i< TRANSITIONS_COUNT; i++){
            if (transitions[i] == 1) {
                indexTransitions.add(i);
            }
        }
        log.debug("indexTransitions: " + indexTransitions);
        return indexTransitions;
    }

    public void increaseFireBranch(int transition){
        if (transition%2==0){
            rightBranchCount++;
        }else{
            leftBranchCount++;
        }
    }




}
