package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.nnf.pns.Main;
import org.nnf.pns.util.Constants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy implements Policy {
    private static BalancedPolicy instance;
    private int leftBranchCount = 0;
    private int rightBranchCount = 0;
    private final int[] transitionsCounter=new int[TRANSITIONS_COUNT];
    private static final Logger log = Logger.getLogger(Main.class);

    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public int choose(int[] transitions) {
        int chosenTransition = 0;


        if (leftBranchCount > rightBranchCount || leftBranchCount ==  rightBranchCount ) {
            chosenTransition=findMinimumFire(lookingForTransitions(transitions).stream()
                    .filter(numero -> numero % 2 == 0)
                    .collect(Collectors.toList()));
            rightBranchCount++;

        } else  {
            chosenTransition=findMinimumFire(lookingForTransitions(transitions).stream()
                    .filter(numero -> numero % 2 != 0)
                    .collect(Collectors.toList()));
            leftBranchCount++;
        }
        log.debug("chosenTransition: " + chosenTransition);
        return chosenTransition;
    }

    private int findMinimumFire(List<Integer> transitions) {
        return transitions.stream()
                .min(Comparator.comparingInt(t -> transitionsCounter[t]))
                .map(t -> {
                    transitionsCounter[t]++;
                    return t;
                })
                .orElseThrow(() -> new NoSuchElementException("Empty list"));
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


}
