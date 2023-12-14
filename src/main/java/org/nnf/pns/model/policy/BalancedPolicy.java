package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;
import org.nnf.pns.Main;

import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;
import static org.nnf.pns.util.Constants.TRANSITIONS_COUNT;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy implements Policy {
    private static BalancedPolicy instance;
    private int leftBranchCount = 0;
    private int rightBranchCount = 0;
    private static final Logger log = Logger.getLogger(Main.class);

    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public int choose(int[] transitions) {
        int chosenTransition = 0;

        if (leftBranchCount > rightBranchCount || (leftBranchCount ==  rightBranchCount ) ) {
            chosenTransition = lookingForTransitions(transitions).stream().filter(trans -> trans % 2 == 0).findFirst().orElse(0);
            rightBranchCount++;
        } else  {
            chosenTransition = lookingForTransitions(transitions).stream().filter(trans -> trans % 2 != 0).findFirst().orElse(0);
            leftBranchCount++;
        }
        log.debug("chosenTransition: " + chosenTransition);
        return chosenTransition;
    }

    public List<Integer> lookingForTransitions(int[] transitions){
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
