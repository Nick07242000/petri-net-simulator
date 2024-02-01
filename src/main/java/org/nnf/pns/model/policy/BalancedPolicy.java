package org.nnf.pns.model.policy;

import lombok.NoArgsConstructor;

import java.util.List;

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

        if (transitions.contains(13)) {
            return 13;
        }

        if (transitions.contains(14)) {
            return 14;
        }

        //Balance branches
        int chosenTransition = leftFiredTransitions >= rightFiredTransitions ?
                filterTransitions(transitions, t -> t % 2 == 0) :
                filterTransitions(transitions, t -> t % 2 != 0);

        increaseCounter(chosenTransition);

        return chosenTransition;
    }
}
