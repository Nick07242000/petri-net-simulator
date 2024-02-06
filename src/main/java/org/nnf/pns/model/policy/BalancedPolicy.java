package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static java.util.Arrays.asList;
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
    public int choose(List<Integer> transitions, List<String> firedTransitions) {
        if (transitions.size() == 1)
            return transitions.get(0);

        long leftFired = getFiredTransitions(firedTransitions, asList(1,3,5,7,9,11));
        long rightFired = getFiredTransitions(firedTransitions, asList(2,4,6,8,10,12));

        //Balance branches
        return leftFired >= rightFired ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);
    }

    private int filterTransitions(List<Integer> transitions, Predicate<Integer> filter) {
        List<Integer> filteredTransitions = transitions.stream()
                .filter(filter)
                .collect(collectingAndThen(
                        toList(),
                        collected -> collected.isEmpty() ? transitions : collected));

        return filteredTransitions.get(random.nextInt(filteredTransitions.size()));
    }
}
