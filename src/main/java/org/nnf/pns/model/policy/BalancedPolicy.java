package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BalancedPolicy extends Policy {
    public static Policy getInstance() {
        if (instance == null) instance = new BalancedPolicy();
        return instance;
    }

    @Override
    public int choose(List<Integer> transitions) {
        if (transitions.size() == 1)
            return transitions.get(0);

        //Balance branches
        int chosenTransition = leftFired >= rightFired ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);

        increaseCounter(chosenTransition);

        return chosenTransition;
    }

    private int filterTransitions(List<Integer> transitions, Predicate<Integer> filter) {
        List<Integer> filteredTransitions = transitions.stream()
                .filter(filter)
                .collect(collectingAndThen(
                        toList(),
                        collected -> collected.isEmpty() ? transitions : collected));

        return filteredTransitions.get(random.nextInt(filteredTransitions.size()));
    }

    private void increaseCounter(int transition) {
        if (transition == 0 || transition == 13 || transition == 14)
            return;

        if (transition % 2 == 0) rightFired++;
        else leftFired++;

        log.debug("Branch executions: L[" + leftFired + "] R[" + rightFired + "]");
    }
}
