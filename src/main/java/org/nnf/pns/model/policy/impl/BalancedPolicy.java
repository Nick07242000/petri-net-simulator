package org.nnf.pns.model.policy.impl;

import lombok.NoArgsConstructor;
import org.nnf.pns.model.policy.Policy;

import java.util.ArrayList;

import static lombok.AccessLevel.PRIVATE;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy implements Policy {
    private static BalancedPolicy instance;
    private int leftBranchCount = 0;
    private int rightBranchCount = 0;

    public static Policy getInstance() {
        return instance == null ?
                new BalancedPolicy() :
                instance;
    }

    @Override
    public int choose(int[] transitions) {
        int chosenTransition = 0;

        if (leftBranchCount > rightBranchCount || (leftBranchCount == 0 && rightBranchCount == 0) ) {
            chosenTransition = lookingForTransitions(transitions).stream().filter(trans -> trans % 2 == 0).findFirst().orElse(-1);
            rightBranchCount++;
        } else if (leftBranchCount < rightBranchCount) {
            chosenTransition = lookingForTransitions(transitions).stream().filter(trans -> trans % 2 != 0).findFirst().orElse(-1);
            leftBranchCount++;
        }
        return chosenTransition;
    }

    public ArrayList<Integer> lookingForTransitions(int[] transitions){
        ArrayList<Integer> indexTransitions = new ArrayList<>();

        for (int i = 0; i< TRANSITIONS_COUNT; i++){
            if (transitions[i] == 1) {
                indexTransitions.add(i);
            }
        }

        return indexTransitions;
    }
}
