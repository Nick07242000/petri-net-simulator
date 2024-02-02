package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class WeightedPolicy extends Policy {
    private int leftFiredThirdSegment = 0;
    private int rightFiredThirdSegment = 0;

    public static Policy getInstance() {
        if (instance == null) instance = new WeightedPolicy();
        return instance;
    }

    @Override
    public int choose(List<Integer> transitions) {
        if (transitions.size() == 1)
            return transitions.get(0);

        boolean determinant = transitions.contains(9) || transitions.contains(10) ?
                leftFiredThirdSegment >= 4 * rightFiredThirdSegment :
                leftFired >= rightFired;

        int chosenTransition = determinant ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);

        increaseCounter(chosenTransition);

        return chosenTransition;
    }

    private int filterTransitions(List<Integer> transitions, Predicate<Integer> filter) {
        if (transitions.stream()
                .filter(filter)
                .anyMatch(t -> t == 9)) return 9;

        if (transitions.stream()
                .filter(filter)
                .anyMatch(t -> t == 10)) return 10;

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

        if (transition == 9) {
            leftFiredThirdSegment++;
            return;
        }

        if (transition == 10) {
            rightFiredThirdSegment++;
            return;
        }

        if (transition % 2 == 0) rightFired++;
        else leftFired++;

        log.debug("Branch executions: L[" + leftFired + "] R[" + rightFired + "]");
        log.debug("Third Segment executions: L[" + leftFiredThirdSegment + "] R[" + rightFiredThirdSegment + "]");
    }
}
