package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy implements Policy {
    private static final Logger log = Logger.getLogger(BalancedPolicy.class);
    private static BalancedPolicy instance;
    private int leftBranchCount = 0;
    private int rightBranchCount = 0;

    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public synchronized int choose(List<Integer> transitions) {
        //If no transitions are sensitized choose T0
        log.debug("TAMAÑO DEL TRANSITION: "+transitions.size());
        if (transitions.size() == 1)
            return transitions.get(0);

        //Balance branches
        int chosenTransition = leftBranchCount >= rightBranchCount ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);

        increaseCounter(chosenTransition);

        return chosenTransition;
    }

    private int filterTransitions(List<Integer> transitions, Predicate<Integer> filter) {
        return transitions.stream()
                .filter(filter)
                .findFirst()
                .orElse(0);
    }

    private void increaseCounter(int transition) {
        if (transition % 2 == 0) rightBranchCount++;
        else leftBranchCount++;

        log.debug("Branch executions: L[" + leftBranchCount + "] R[" + rightBranchCount + "]");
    }
}
